// 헤더와 알림 관련 UI 로직을 처리하는 스크립트 파일
$(document).ready(function () {
    // 검색 버튼 클릭: 검색 오버레이 표시 및 포커스 설정
    $('#search-btn').click(function (e) {
        e.preventDefault();
        $('.search-overlay').fadeIn();
        $('#searchInput').focus();
    });

    // 검색 닫기 버튼 클릭: 검색 오버레이 닫기 및 입력값 초기화
    $('#close-search').click(function () {
        $('.search-overlay').fadeOut();
        $('#searchInput').val('');
    });

    // 검색 입력창에서 Enter 키 입력: 검색 실행
    $('#searchInput').on('keypress', function (e) {
        if (e.which === 13) {
            e.preventDefault();
            const keyword = $(this).val().trim();
            if (keyword) {
                search(keyword);
            }
        }
    });

    // 알림 아이콘 클릭 시 드롭다운 표시: 알림 목록 표시/숨김 토글
    $('#notificationBell').click(function (e) {
        e.preventDefault();
        const dropdown = $('#notificationDropdown');
        if (dropdown.is(':visible')) {
            dropdown.hide();
        } else {
            loadNotifications();
            dropdown.show();
            $(document).one('click', function (e) {
                if (!$(e.target).closest('#notificationBell').length) {
                    dropdown.hide();
                }
            });
        }
    });

    // 개별 알림 삭제 버튼 클릭: 특정 알림 삭제 및 UI 갱신
    $(document).on('click', '.close-btn', function (e) {
        e.stopPropagation();
        const notificationId = $(this).data('id');
        deleteNotification(notificationId);
        $(this).closest('.notification-item').remove();
        window.notificationCount--;
        updateNotificationBadge();
    });

    // 모두 읽음 버튼 클릭: 모든 알림 읽음 처리 및 UI 갱신
    $(document).on('click', '#markAllRead', function () {
        $('.notification-item').addClass('read').find('.close-btn').show();
        updateNotificationsReadStatus();
    });

    // 모두 삭제 버튼 클릭: 모든 알림 삭제 및 UI 갱신
    $(document).on('click', '#deleteAll', function () {
        $('#notificationDropdown').empty();
        deleteAllNotifications();
        window.notificationCount = 0;
        updateNotificationBadge();
        addNotificationActions();
    });
});

// 검색 요청 실행: 키워드로 검색 요청 보내기
function search(keyword) {
    $.ajax({
        url: '/search',
        type: 'POST',
        data: { keyword: keyword },
        dataType: 'json',
        success: function (result) {
            if (result.success) {
                window.location.href = '/search/result?keyword=' + encodeURIComponent(keyword);
            } else {
                alert('검색 처리에 실패했습니다: ' + (result.message || '알 수 없는 오류'));
            }
        },
        error: function (xhr, status, error) {
            console.error('검색 오류:', error);
            alert('검색 중 오류가 발생했습니다: ' + error);
        }
    });
}

// 알림 개수 뱃지 갱신: 알림 개수를 UI에 반영
function updateNotificationBadge() {
    const notification = $('.notification');
    if (window.notificationCount > 0) {
        notification.attr('data-count', window.notificationCount);
        notification.css('display', 'flex');
    } else {
        notification.attr('data-count', '0');
        notification.css('display', 'flex');
    }
}

// 알림 항목 추가: 드롭다운에 알림 항목 추가
function addNotificationItem(notificationId, message) {
    console.log("알림 항목 추가 - ID:", notificationId, "메시지:", message);
    const dropdown = $('#notificationDropdown');
    dropdown.find('.notification-actions').remove();
    const item = $(`<div class="notification-item" data-id="${notificationId}"><span>${message}</span><button class="close-btn" data-id="${notificationId}">X</button></div>`);
    item.click(function (e) {
        if (!$(e.target).hasClass('close-btn')) {
            window.location.href = '/notice/list';
        }
    });
    dropdown.prepend(item);
    addNotificationActions();
}

// 알림 작업 버튼 추가: "모두 읽음" 및 "모두 삭제" 버튼 추가
function addNotificationActions() {
    const dropdown = $('#notificationDropdown');
    if (dropdown.find('.notification-actions').length === 0) {
        const actions = $(`<div class="notification-actions"><button id="markAllRead">모두 읽음</button><button id="deleteAll" class="secondary">모두 삭제</button></div>`);
        dropdown.append(actions);
    }
}

// 알림 목록 로드: 서버에서 알림 목록 불러오기
function loadNotifications() {
    console.log("알림 목록 요청 시작: /api/notifications");
    $.ajax({
        url: '/api/notifications',
        type: 'GET',
        dataType: 'json',
        xhrFields: {
            withCredentials: true // 세션 쿠키 포함
        },
        success: function (data) {
            console.log("알림 목록 데이터 수신:", data);
            $('#notificationDropdown').empty();
            window.notificationCount = data ? data.length || 0 : 0;
            updateNotificationBadge();
            if (Array.isArray(data)) {
                if (data.length === 0) {
                    console.log("알림이 없습니다.");
                    const noNotificationsItem = $('<div class="notification-item">알림이 없습니다.</div>');
                    $('#notificationDropdown').prepend(noNotificationsItem);
                } else {
                    data.forEach(notification => {
                        const message = notification.message || '새로운 공지사항이 있습니다.';
                        addNotificationItem(notification.notificationId, message);
                    });
                }
            } else {
                console.warn('예상치 못한 데이터 형식:', data);
                const errorItem = $('<div class="notification-item">데이터 형식 오류</div>');
                $('#notificationDropdown').prepend(errorItem);
            }
        },
        error: function (xhr, status, error) {
            console.error('알림 목록 로드 실패:', error, '상태:', xhr.status, '응답:', xhr.responseText);
            const errorItem = $('<div class="notification-item">알림 로드 실패 (상태: ' + xhr.status + ')</div>');
            $('#notificationDropdown').prepend(errorItem);
            if (xhr.status === 401) {
                console.warn('사용자 인증 실패. 로그인 페이지로 이동합니다...');
                window.location.href = '/login';
            }
        },
        complete: function (xhr, status) {
            console.log("알림 목록 요청 완료, 상태:", status);
        }
    });
}

// 개별 알림 삭제: 특정 알림 삭제 요청
function deleteNotification(notificationId) {
    $.ajax({
        url: '/api/notifications/' + notificationId,
        type: 'DELETE',
        xhrFields: {
            withCredentials: true // 세션 쿠키 포함
        },
        success: function () {
            console.log('알림 삭제 성공:', notificationId);
            window.notificationCount--;
            updateNotificationBadge();
            $('#notificationDropdown').empty();
            loadNotifications(); // 갱신
        },
        error: function (xhr, status, error) {
            console.error('알림 삭제 실패:', error, '상태:', xhr.status, '응답:', xhr.responseText);
        }
    });
}

// 모든 알림 삭제: 모든 알림 삭제 요청
function deleteAllNotifications() {
    $.ajax({
        url: '/api/notifications',
        type: 'DELETE',
        xhrFields: {
            withCredentials: true // 세션 쿠키 포함
        },
        success: function () {
            console.log('모든 알림 삭제 성공');
            window.notificationCount = 0;
            updateNotificationBadge();
            $('#notificationDropdown').empty();
            loadNotifications(); // 갱신
        },
        error: function (xhr, status, error) {
            console.error('모든 알림 삭제 실패:', error, '상태:', xhr.status, '응답:', xhr.responseText);
        }
    });
}

// 모든 알림 읽음 처리: 모든 알림 읽음 상태로 변경 요청
function updateNotificationsReadStatus() {
    $.ajax({
        url: '/api/notifications/read',
        type: 'POST',
        xhrFields: {
            withCredentials: true // 세션 쿠키 포함
        },
        success: function () {
            console.log('모든 알림 읽음 처리 성공');
            $('.notification-item').addClass('read').find('.close-btn').show();
        },
        error: function (xhr, status, error) {
            console.error('모든 알림 읽음 처리 실패:', error, '상태:', xhr.status, '응답:', xhr.responseText);
        }
    });
}