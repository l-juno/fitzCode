package kr.co.fitzcode.user.service;

import kr.co.fitzcode.common.dto.AccountDTO;
import kr.co.fitzcode.common.dto.CouponDTO;
import kr.co.fitzcode.common.dto.OrderDTO;
import kr.co.fitzcode.common.dto.UserDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MypageService {
    // 사용자 정보
    UserDTO getMyInfo(int userId);
    // 구매내역 & 운송장 정보
    List<OrderDTO> getOrderList(int userId);

    // 사용자 계좌 정보
    AccountDTO getUserAccount(int userId);

    // 계좌 정보 변경(수정)
    void updateAccountData(AccountDTO accountDTO);

    // 사용자 쿠폰 정보
    List<CouponDTO> getUserCoupon(int userId);

    // 회원정보 업데이트
    void updateUserInfo(UserDTO userDTO);

    // 프로필 수정
    void updateProfile(UserDTO userDTO, MultipartFile profileImage);

    // 회원 인증
    UserDTO verifyUser(UserDTO userDTO);
}
