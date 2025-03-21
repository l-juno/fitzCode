document.addEventListener("DOMContentLoaded", () => {

    // 계좌 추가 모달 여닫기
    const addAccountModal = document.getElementById("add-account-modalContainer");
    const insertBtn = document.getElementById("insert-btn");

    insertBtn.addEventListener("click", (event) => {
        event.preventDefault();  // 폼 제출 방지

        // 모달 창 열기
        addAccountModal.classList.remove("hidden");
        document.body.style.overflow = "hidden";

        // 모달 초기화 (값 초기화)
        document.querySelectorAll(".accountHolder").forEach(input => input.value = "");
        document.querySelectorAll(".accountNumber").forEach(input => input.value = "");
        document.querySelectorAll(".checkBtn").forEach(btn => {
            btn.style.background = "white";
            btn.style.color = "black";
            btn.style.border = "#ddd 1px solid";
        });
    });

    // 모달창 닫기
    function closeModal() {
        addAccountModal.classList.add("hidden");
        document.body.style.overflow = "auto";
    }

    document.getElementById("add-account-modalCloseButton").addEventListener("click", closeModal);
    document.getElementById("saveBtn").addEventListener("click", closeModal);


    // 계좌 추가
    // document.getElementById("saveBtn").addEventListener("click", () => {
    //     let accountHolder = document.getElementById("addAccountHolder").value;
    //     let bankName = document.getElementById("addBankName").value;
    //     let accountNumber = document.getElementById("addAccountNumber").value;
    //     let userId = document.getElementById("userId").value;
    //     console.log(accountHolder);
    //     console.log(bankName);
    //     console.log(accountNumber);
    //     console.log(userId);
    //     let data = {
    //         accountHolder: accountHolder,
    //         bankName: bankName,
    //         accountNumber: accountNumber,
    //         userId: userId,
    //         isDefault: false
    //     };
    //
    //     fetch("http://localhost:8080/mypage/insertAccount", {
    //         method: "POST",
    //         headers: {
    //             "Content-Type": "application/json"
    //         },
    //         body: JSON.stringify(data)
    //     })
    //         .then(data => {
    //             location.href = "http://localhost:8080/mypage/account";  // 리다이렉트
    //         })
    //
    // });

});