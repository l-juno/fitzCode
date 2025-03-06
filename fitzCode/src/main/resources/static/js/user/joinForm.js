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

    // 이메일이 비어있지 않으면 중복 체크
    fetch(`checkEmail?email=${email}`)
        .then(response => response.text())
        .then(data => {
            messageElement.textContent = data;
            messageElement.style.color = data.includes("사용 가능") ? "green" : "red";
        })
        .catch(error => console.error("Error:", error));
}


// 닉네임 중복 체크
function checkNickname() {
    let nickName = document.getElementById("nickName").value;
    let messageElement = document.getElementById("nicknameMessage");

    // 닉네임 비어 있는지 확인
    if (nickName.trim() === "") {
        messageElement.textContent = "닉네임을 입력하세요";
        messageElement.style.color = "red";
        return;
    }

    // 닉네임이 비어있지 않으면 중복 체크
    fetch(`checkNickname?nickName=${nickName}`)
        .then(response => response.text())
        .then(data => {
            messageElement.textContent = data;
            messageElement.style.color = data.includes("사용 가능") ? "green" : "red";
        })
        .catch(error => console.error("Error:", error));
}
