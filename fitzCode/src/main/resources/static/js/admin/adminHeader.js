let timeLeft = 1800;

const storedTimeLeft = localStorage.getItem('sessionTimeLeft');
if (storedTimeLeft) {
    timeLeft = parseInt(storedTimeLeft, 10);
}

window.addEventListener('load', function() {
    fetch('/admin/session-info', {
        method: 'GET',
        headers: { 'Accept': 'application/json' }
    })
        .then(response => response.json())
        .then(data => {
            const creationTime = data.creationTime;
            const maxInactiveInterval = 1800;
            const currentTime = Date.now();
            const elapsedTime = Math.floor((currentTime - creationTime) / 1000);
            timeLeft = Math.max(maxInactiveInterval - elapsedTime, 0);
            localStorage.setItem('sessionTimeLeft', timeLeft);
            updateTimer();
        })
        .catch(error => {
            console.error('Error fetching session info:', error);
            updateTimer();
        });
});

const timerElement = document.getElementById('session-timer');
const extendButton = document.getElementById('extend-session-btn');

function updateTimer() {
    let minutes = Math.floor(timeLeft / 60);
    let seconds = timeLeft % 60;
    timerElement.textContent = `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;

    if (timeLeft <= 0) {
        alert("세션이 만료되었습니다. 다시 로그인해 주세요.");
        window.location.href = "/login?expired";
        return;
    } else if (timeLeft <= 30) { // 30초 전 경고
        timerElement.style.color = "red";
        if (!timerElement.classList.contains('warning')) {
            alert("세션이 30초 후 만료됩니다. 작업을 저장하세요!");
            timerElement.classList.add('warning');
        }
    }

    timeLeft--;
    localStorage.setItem('sessionTimeLeft', timeLeft);
}

extendButton.addEventListener('click', function() {
    fetch('/admin/extend-session', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (response.ok) {
                timeLeft = 1800;
                timerElement.style.color = "#000000";
                timerElement.classList.remove('warning');
                localStorage.setItem('sessionTimeLeft', timeLeft);
                updateTimer();
                alert("세션이 30분 연장되었습니다.");
            } else {
                alert("세션 연장에 실패했습니다. 다시 로그인해 주세요.");
            }
        })
        .catch(error => {
            console.error('Error extending session:', error);
            alert("세션 연장에 실패했습니다. 다시 로그인해 주세요.");
        });
});

setInterval(updateTimer, 1000);

setInterval(() => {
    fetch('/admin/session-info', {
        method: 'GET',
        headers: { 'Accept': 'application/json' }
    })
        .then(response => response.json())
        .then(data => {
            const creationTime = data.creationTime;
            const maxInactiveInterval = 1800;
            const currentTime = Date.now();
            const elapsedTime = Math.floor((currentTime - creationTime) / 1000);
            const newTimeLeft = Math.max(maxInactiveInterval - elapsedTime, 0);
            if (newTimeLeft !== timeLeft) {
                timeLeft = newTimeLeft;
                localStorage.setItem('sessionTimeLeft', timeLeft);
                updateTimer();
            }
        })
        .catch(error => console.error('Error syncing session time:', error));
}, 60000);