window.notificationCount = 0;

// 페이지 로드 시 초기 알림 개수
$(document).ready(function () {
    loadNotifications(); // 알림 목록 불러옴
});

// 알림 구독 시작: SSE 사용해서  공지사항 생성 이벤트 수신할거임
function subscribeToNotifications() {
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
        setTimeout(subscribeToNotifications, 1000);
    };
}

// 알림 메시지를 화면에 추가: 알림 메시지를 화면 하단에 표시
function addNotification(message) {
    const div = document.createElement("div");
    div.textContent = `${new Date().toLocaleTimeString()} - ${message}`;
    document.getElementById("notifications").appendChild(div);
}

// 알림 서버 저장: 새로운 알림을 서버에 저장 요청
function saveNotification(notice) {
    $.ajax({
        url: '/api/notifications',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            userId: null,
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

// 알림 구독 시작: 페이지 로드 시 알림 구독 시작
window.onload = () => subscribeToNotifications();