document.addEventListener("DOMContentLoaded", () => {

    // 새 주소 추가 모달 여닫기
    const addAddressModal = document.getElementById("add-address-modalContainer");
    const insertBtn = document.getElementById("insert-btn");
    const addAddressModalCloseButton = document.getElementById("add-address-modalCloseButton");

    insertBtn.addEventListener("click", (event) => {
        event.preventDefault();  // 폼 제출 방지

        // 모달 창 열기
        addAddressModal.classList.remove("hidden");
        document.body.style.overflow = "hidden";

        // 모달 초기화 (값 초기화)
        document.querySelectorAll(".inputText").forEach(input => input.value = "");
        document.getElementById("name-alert").textContent = "";
        document.querySelectorAll(".checkBtn").forEach(btn => {
            btn.style.background = "white";
            btn.style.color = "black";
            btn.style.border = "#ddd 1px solid";
        });
    });

    addAddressModalCloseButton.addEventListener("click", () => {
        addAddressModal.classList.add("hidden");
        document.body.style.overflow = "auto";
    });

    // alert 올바른 이름 입력 메시지
    let nameInput = document.getElementById("input-name");
    nameInput.addEventListener("input", () => {
        if (nameInput.value.trim() !== "") {
            document.getElementById("name-alert").textContent = "올바른 이름을 입력해주세요.(2-20자)";
        }
    });

    // 주소 API 불러오기
    let postalCode = "";
    let addressLine1 = "";

    document.getElementById("searchAddress").addEventListener("click", () => {
        new daum.Postcode({
            oncomplete: function(data) {
                document.getElementById("input-postalCode").value = data.zonecode;
                document.getElementById("input-address").value = data.roadAddress;
                postalCode = data.zonecode;
                addressLine1 = data.roadAddress;
            }
        }).open();
    });

    // 기본 배송지 체크 여부
    let cnt = 0;
    let check = false;

    document.querySelectorAll(".checkBtn").forEach(btn => {
        btn.addEventListener("click", () => {
            cnt++;
            check = (cnt % 2 !== 0);
            btn.style.background = check ? "black" : "white";
            btn.style.color = check ? "white" : "black";
            btn.style.border = check ? "black 1px solid" : "#ddd 1px solid";
        });
    });

    // 새 주소 저장
    document.getElementById("saveBtn").addEventListener("click", () => {
        let addressLine2 = document.getElementById("input-detailAddress").value;
        let data = {};

        if (!check) {
            data = {
                addressLine1: addressLine1,
                addressLine2: addressLine2,
                postalCode: postalCode,
                isDefault: false
            };
        } else {
            data = {
                defaultAddressLine1: addressLine1,
                defaultAddressLine2: addressLine2,
                defaultPostalCode: postalCode,
                isDefault: true
            };
        }

        fetch("http://localhost:8080/address/insertAddress", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        });
    });

    // 삭제할 주소 미체크 방지
    document.getElementById("deleteBtn").addEventListener("click", function(event) {
        // 체크박스 선택 여부 확인
        var checkboxes = document.querySelectorAll('input[name="addressId"]:checked');

        // 선택된 체크박스가 없으면 알림 띄우기
        if (checkboxes.length === 0) {
            event.preventDefault();
            alert("삭제할 주소를 선택해주세요.");
        }
    });

});
