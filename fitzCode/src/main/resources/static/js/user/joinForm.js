// 비밀번호와 비밀번호 확인 일치 체크
function validateForm() {
    var password = document.getElementById('password').value;
    var passwordConfirm = document.getElementById('password_confirm').value;
    var passwordError = document.getElementById('passwordError');

    if (password !== passwordConfirm) {
        passwordError.textContent = '비밀번호와 비밀번호 확인이 일치하지 않습니다.';
        return false;
    } else {
        passwordError.textContent = '';
    }
    return true;
}

document.addEventListener("DOMContentLoaded", function () {
    const agreeAll = document.getElementById("agree-all");
    const agreeChecks = document.querySelectorAll(".agree-check[data-required='true']");
    const signupBtn = document.getElementById("signup-btn");

    // 전체 동의 체크 시 모든 항목 체크
    agreeAll.addEventListener("change", function () {
        document.querySelectorAll(".agree-check").forEach(checkbox => {
            checkbox.checked = agreeAll.checked;
        });
        toggleSignupButton();
    });

    // 개별 체크박스 변경 시 전체 동의 체크박스 상태 변경
    document.querySelectorAll(".agree-check").forEach(checkbox => {
        checkbox.addEventListener("change", function () {
            agreeAll.checked = [...document.querySelectorAll(".agree-check")].every(cb => cb.checked);
            toggleSignupButton();
        });
    });

    // 필수 항목 체크 여부에 따라 회원가입 버튼 활성화
    function toggleSignupButton() {
        const allChecked = [...agreeChecks].every(cb => cb.checked);
        if (allChecked) {
            signupBtn.classList.add("active");
            signupBtn.removeAttribute("disabled");
        } else {
            signupBtn.classList.remove("active");
            signupBtn.setAttribute("disabled", "true");
        }
    }
});

// 이메일 유효성 검사 함수
function isValidEmail(email) {
    // 이메일 형식 정규 표현식
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return emailRegex.test(email);
}

// 이메일 중복 체크
function checkEmail() {
    let email = document.getElementById("email").value;
    let messageElement = document.getElementById("emailMessage");

    // 이메일이 비어있는지 확인
    if (email.trim() === "") {
        messageElement.textContent = "이메일을 입력하세요";
        messageElement.style.color = "red";
        return;
    }

    // 이메일 형식 유효성 검사
    if (!isValidEmail(email)) {
        messageElement.textContent = "유효한 이메일 형식이 아닙니다.";
        messageElement.style.color = "red";
        return;
    }

    fetch(`/checkEmail?email=${encodeURIComponent(email)}`, {
        method: 'GET'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('서버 응답 오류: ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            if (data.available) {
                messageElement.textContent = "사용 가능한 이메일입니다.";
                messageElement.style.color = "green";
            } else {
                messageElement.textContent = "이미 사용 중인 이메일입니다.";
                messageElement.style.color = "red";
            }
        })
        .catch(error => {
            console.error("이메일 중복 확인 오류:", error);
            messageElement.textContent = "이메일 중복 확인 중 오류가 발생했습니다.";
            messageElement.style.color = "red";
        });
}

// 닉네임 중복 체크
function checkNickname() {
    let nickname = document.getElementById("nickname").value;
    let messageElement = document.getElementById("nicknameMessage");

    // 닉네임 비어 있는지 확인
    if (nickname.trim() === "") {
        messageElement.textContent = "닉네임을 입력하세요";
        messageElement.style.color = "red";
        return;
    }

    // 닉네임 길이 3글자 이상인지 확인
    if (nickname.length < 3) {
        messageElement.textContent = "닉네임은 3글자 이상이어야 합니다.";
        messageElement.style.color = "red";
        return;
    }

    fetch(`/checkNickname?nickname=${encodeURIComponent(nickname)}`, {
        method: 'GET'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('서버 응답 오류: ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            if (data.available) {
                messageElement.textContent = "사용 가능한 닉네임입니다.";
                messageElement.style.color = "green";
            } else {
                messageElement.textContent = "이미 사용 중인 닉네임입니다.";
                messageElement.style.color = "red";
            }
        })
        .catch(error => {
            console.error("닉네임 중복 확인 오류:", error);
            messageElement.textContent = "닉네임 중복 확인 중 오류가 발생했습니다.";
            messageElement.style.color = "red";
        });
}