window.notificationCount = 0;

// 인증 상태 체크 함수
function checkAuthenticated(callback) {
    $.ajax({
        url: '/api/user/check', // 인증 상태를 확인하는 엔드포인트
        type: 'GET',
        success: function(response) {
            if (response.authenticated) {
                callback(true);
            } else {
                callback(false);
            }
        },
        error: function() {
            callback(false); // 오류 발생 시 인증안된 사용자라고 생각하고
        }
    });
}

// 페이지 로드 시 초기 알림 개수
$(document).ready(function () {
    checkAuthenticated(function(isAuthenticated) {
        if (isAuthenticated) {
            loadNotifications(); // 인증된 경우에만 알림 로드
        }
    });
});

// 알림 구독 시작: SSE 사용해서 공지사항 생성 이벤트 수신
function subscribeToNotifications() {
    checkAuthenticated(function(isAuthenticated) {
        if (!isAuthenticated) {
            console.log("로그인하지 않은 사용자입니다. 알림 구독을 비활성화합니다.");
            return; // 비인증 사용자면 중단
        }

        const eventSource = new EventSource("/admin/notice/subscribe");

        eventSource.onopen = () => {
            console.log("알림 연결이 시작되었습니다: " + new Date().toLocaleTimeString());
        };

        eventSource.addEventListener("NOTICE_CREATED", (event) => {
            console.log("공지사항 생성 이벤트 수신: " + new Date().toLocaleTimeString());
            console.log("이벤트 데이터:", event.data);
            try {
                const notice = JSON.parse(event.data);
                console.log("파싱된 공지사항:", notice);
                if (notice && notice.title) {
                    const notificationMessage = "새로운 공지사항이 있습니다.";
                    addNotification(notificationMessage);
                    window.notificationCount++;
                    updateNotificationBadge();
                    saveNotification(notice);
                } else {
                    console.error("공지사항 데이터 오류: 제목이 없습니다.", notice);
                }
            } catch (e) {
                console.error("JSON 파싱 오류:", e, "데이터:", event.data);
            }
        });

        eventSource.onerror = (error) => {
            console.error("알림 연결 오류 발생: " + new Date().toLocaleTimeString(), error);
            eventSource.close();
            setTimeout(subscribeToNotifications, 1000); // 재연결 시도
        };
    });
}

// 알림 메시지를 화면에 추가
function addNotification(message) {
    const div = document.createElement("div");
    div.textContent = `${new Date().toLocaleTimeString()} - ${message}`;
    document.getElementById("notifications").appendChild(div);
}

// 알림 서버 저장
function saveNotification(notice) {
    $.ajax({
        url: '/api/notifications',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            userId: null, // 인증 상태에 따라 동적으로 설정 가능
            type: 1,
            message: "새로운 공지사항이 있습니다.",
            relatedId: notice.noticeId
        }),
        success: function () {
            console.log('알림 저장 성공');
        },
        error: function (xhr, status, error) {
            console.error('알림 저장 실패:', error, '상태:', xhr.status, '응답:', xhr.responseText);
        }
    });
}

// 알림 구독 시작: 페이지 로드 시 인증 상태 체크 후 구독
window.onload = () => {
    checkAuthenticated(function(isAuthenticated) {
        if (isAuthenticated) {
            subscribeToNotifications();
        }
    });
};

// 알림 배지 업데이트 (기존 코드 유지)
function updateNotificationBadge() {
    const notificationElement = document.querySelector('.notification');
    if (notificationElement) {
        notificationElement.setAttribute('data-count', window.notificationCount);
    }
}

// 알림 로드 함수 (인증 상태에 따라 호출)
function loadNotifications() {
    checkAuthenticated(function(isAuthenticated) {
        if (isAuthenticated) {
            // 인증된 사용자에 대한 알림 로드 로직 추가
            console.log("알림 로드 시작");
        }
    });
}