// 헤더와 알림 관련 UI 로직을 처리하는 스크립트 파일
let cartUpdateInterval;

// 인증 상태 체크 함수
function checkAuthenticated(callback) {
    $.ajax({
        url: '/api/user/check',
        type: 'GET',
        success: function(response) {
            console.log("인증 상태 체크 응답:", response);
            if (response.authenticated) {
                callback(true);
            } else {
                callback(false);
            }
        },
        error: function(xhr, status, error) {
            console.error("인증 상태 체크 오류:", error, "상태:", xhr.status, "응답:", xhr.responseText);
            callback(false);
        }
    });
}

$(document).ready(function () {
    checkAuthenticated(function(isAuthenticated) {
        if (!isAuthenticated) {
            console.log("로그인하지 않은 사용자 : 이벤트 처리를 비활성화");
            return;
        }

        // 알림 아이콘 클릭 시 드롭다운 표시
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

        // 개별 알림 삭제
        $(document).on('click', '.close-btn', function (e) {
            e.stopPropagation();
            const notificationId = $(this).data('id');
            deleteNotification(notificationId);
            $(this).closest('.notification-item').remove();
            window.notificationCount--;
            updateNotificationBadge();
        });

        // 모두 읽음 버튼
        $(document).on('click', '#markAllRead', function () {
            $('.notification-item').addClass('read').find('.close-btn').show();
            updateNotificationsReadStatus();
        });

        // 모두 삭제 버튼
        $(document).on('click', '#deleteAll', function () {
            $('#notificationDropdown').empty();
            deleteAllNotifications();
            window.notificationCount = 0;
            updateNotificationBadge();
            addNotificationActions();
        });

        // 검색 입력창 Enter 키
        $('.input').on('keypress', function (e) {
            if (e.which === 13) {
                e.preventDefault();
                const keyword = $(this).val().trim();
                if (keyword) {
                    search(keyword);
                }
            }
        });

        // 초기 카트 수량 로드
        updateCartCount();
        cartUpdateInterval = setInterval(updateCartCount, 10000);

        // 가상 상품 추가 이벤트
        $('#addToCartButton').on('click', function () {
            addToCart();
        });
    });
});

// 검색 요청 실행
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

// 알림 개수 뱃지 갱신
function updateNotificationBadge() {
    const notification = $('.notification');
    if (notification.length > 0) {
        notification.attr('data-count', window.notificationCount);
        if (window.notificationCount > 0) {
            notification.css('display', 'flex');
        } else {
            notification.css('display', 'flex');
        }
    }
}

// 카트 수량 업데이트
function updateCartCount() {
    checkAuthenticated(function(isAuthenticated) {
        if (!isAuthenticated) {
            console.log("로그인하지 않은 사용자 : 카트 수량 업데이트 중단");
            $('#ex4 .p1').attr('data-count', '0');
            return;
        }

        $.ajax({
            url: '/api/cart/count',
            type: 'GET',
            dataType: 'json',
            xhrFields: { withCredentials: true },
            success: function (data) {
                if (data && data.count !== undefined) {
                    const cartCount = data.count || 0;
                    $('#ex4 .p1').attr('data-count', cartCount);
                    console.log('카트 수량 업데이트 완료: ' + cartCount);
                } else {
                    console.warn('카트 수량 데이터가 없습니다.');
                    $('#ex4 .p1').attr('data-count', '0');
                }
            },
            error: function (xhr, status, error) {
                console.error('카트 수량 로드 실패:', error, '상태:', xhr.status, '응답:', xhr.responseText);
                $('#ex4 .p1').attr('data-count', '0');
            }
        });
    });
}

// 상품을 카트에 추가
function addToCart() {
    checkAuthenticated(function(isAuthenticated) {
        if (!isAuthenticated) {
            console.log("로그인하지 않은 사용자 : 카트 추가를 중단");
            return;
        }

        $.ajax({
            url: '/api/cart/add',
            type: 'POST',
            data: { productId: 1 },
            dataType: 'json',
            xhrFields: { withCredentials: true },
            success: function (response) {
                if (response.success) {
                    console.log('상품 추가 성공, 카트 수량 갱신');
                    updateCartCount();
                } else {
                    console.error('상품 추가 실패:', response.message);
                }
            },
            error: function (xhr, status, error) {
                console.error('상품 추가 오류:', error);
            }
        });
    });
}

// 알림 항목 추가
function addNotificationItem(notificationId, message) {
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

// 알림 작업 버튼 추가
function addNotificationActions() {
    const dropdown = $('#notificationDropdown');
    if (dropdown.find('.notification-actions').length === 0) {
        const actions = $(`<div class="notification-actions"><button id="markAllRead">모두 읽음</button><button id="deleteAll" class="secondary">모두 삭제</button></div>`);
        dropdown.append(actions);
    }
}

// 알림 목록 로드
function loadNotifications() {
    checkAuthenticated(function(isAuthenticated) {
        if (!isAuthenticated) {
            console.log("로그인하지 않은 사용자 : 알림 로드 중단");
            $('#notificationDropdown').empty();
            $('#notificationDropdown').prepend('<div class="notification-item">로그인 후 알림을 확인하세요.</div>');
            return;
        }

        console.log("알림 로드 시작");
        $.ajax({
            url: '/api/notifications',
            type: 'GET',
            dataType: 'json',
            xhrFields: { withCredentials: true },
            success: function (data) {
                console.log("알림 목록 데이터 수신:", data);
                $('#notificationDropdown').empty();
                window.notificationCount = data ? data.length || 0 : 0;
                updateNotificationBadge();
                if (Array.isArray(data)) {
                    if (data.length === 0) {
                        console.log("알림이 없습니다.");
                        $('#notificationDropdown').prepend('<div class="notification-item">알림이 없습니다.</div>');
                    } else {
                        data.forEach(notification => {
                            console.log("알림 항목:", notification);
                            const message = notification.message || '새로운 공지사항이 있습니다.';
                            addNotificationItem(notification.notificationId, message);
                        });
                    }
                } else {
                    console.warn('예상치 못한 데이터 형식:', data);
                    $('#notificationDropdown').prepend('<div class="notification-item">데이터 형식 오류</div>');
                }
            },
            error: function (xhr, status, error) {
                console.error('알림 목록 로드 실패:', error, '상태:', xhr.status, '응답:', xhr.responseText);
                $('#notificationDropdown').prepend('<div class="notification-item">알림 로드 실패 (상태: ' + xhr.status + ')</div>');
            }
        });
    });
}

// 개별 알림 삭제
function deleteNotification(notificationId) {
    checkAuthenticated(function(isAuthenticated) {
        if (!isAuthenticated) {
            console.log("로그인하지 않은 사용자 : 알림 삭제 중단");
            return;
        }

        $.ajax({
            url: '/api/notifications/' + notificationId,
            type: 'DELETE',
            xhrFields: { withCredentials: true },
            success: function () {
                console.log('알림 삭제 성공:', notificationId);
                window.notificationCount--;
                updateNotificationBadge();
                $('#notificationDropdown').empty();
                loadNotifications();
            },
            error: function (xhr, status, error) {
                console.error('알림 삭제 실패:', error, '상태:', xhr.status, '응답:', xhr.responseText);
            }
        });
    });
}

// 모든 알림 삭제
function deleteAllNotifications() {
    checkAuthenticated(function(isAuthenticated) {
        if (!isAuthenticated) {
            console.log("로그인하지 않은 사용자 : 모든 알림 삭제를 중단");
            return;
        }

        $.ajax({
            url: '/api/notifications',
            type: 'DELETE',
            xhrFields: { withCredentials: true },
            success: function () {
                console.log('모든 알림 삭제 성공');
                window.notificationCount = 0;
                updateNotificationBadge();
                $('#notificationDropdown').empty();
                loadNotifications();
            },
            error: function (xhr, status, error) {
                console.error('모든 알림 삭제 실패:', error, '상태:', xhr.status, '응답:', xhr.responseText);
            }
        });
    });
}

// 모든 알림 읽음 처리
function updateNotificationsReadStatus() {
    checkAuthenticated(function(isAuthenticated) {
        if (!isAuthenticated) {
            console.log("로그인하지 않은 사용자 : 알림 읽음 처리 중단");
            return;
        }

        $.ajax({
            url: '/api/notifications/read',
            type: 'POST',
            xhrFields: { withCredentials: true },
            success: function () {
                console.log('모든 알림 읽음 처리 성공');
                $('.notification-item').addClass('read').find('.close-btn').show();
            },
            error: function (xhr, status, error) {
                console.error('모든 알림 읽음 처리 실패:', error, '상태:', xhr.status, '응답:', xhr.responseText);
            }
        });
    });
}