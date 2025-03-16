package kr.co.fitzcode.user.mapper;

import kr.co.fitzcode.common.dto.AccountDTO;
import kr.co.fitzcode.common.dto.CouponDTO;
import kr.co.fitzcode.common.dto.OrderDTO;
import kr.co.fitzcode.common.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MypageMapper {
    // 사용자 로그인 정보
    UserDTO getMyInfo(@Param("userId") int userId);

    // 구매내역 & 운송장 정보
    List<OrderDTO> getOrderList(@Param("userId") int userId);

    // 사용자 계좌 정보
    AccountDTO getUserAccount(@Param("userId") int userId);

    // 사용자 쿠폰 정보
    List<CouponDTO> getUserCoupon(@Param("userId") int userId);

    // 사용자 계좌 정보 변경(수정)
    void updateAccountData(@Param("AccountDTO") AccountDTO accountDTO);

    // 회원정보 업데이트
    void updateUserInfo(@Param("userDTO") UserDTO userDTO);
}
