package kr.co.fitzcode.user.service;

import kr.co.fitzcode.common.dto.AccountDTO;
import kr.co.fitzcode.common.dto.CouponDTO;
import kr.co.fitzcode.common.dto.OrderDTO;
import kr.co.fitzcode.common.dto.UserDTO;
import kr.co.fitzcode.user.mapper.MypageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MypageServiceImpl implements MypageService {
    private final MypageMapper mypageMapper;
    private final PasswordEncoder passwordEncoder;

    // 사용자 로그인 정보
    @Override
    public UserDTO getMyInfo(int userId) {
        return mypageMapper.getMyInfo(userId);
    }

    // 구매내역 & 운송장 정보
    @Override
    public List<OrderDTO> getOrderList(int userId) {
        return mypageMapper.getOrderList(userId);
    }

    // 사용자 계좌 정보
    @Override
    public AccountDTO getUserAccount(int userId) {
        return mypageMapper.getUserAccount(userId);
    }

    // 계좌 정보 변경
    @Override
    public void updateAccountData(AccountDTO accountDTO) {
        mypageMapper.updateAccountData(accountDTO);

    }

    // 사용자 쿠폰 정보
    @Override
    public List<CouponDTO> getUserCoupon(int userId) {
        return mypageMapper.getUserCoupon(userId);
    }

    // 회원정보 업데이트
    @Override
    public void updateUserInfo(UserDTO userDTO) {
        // 비밀번호 암호화
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        log.info(">>>>>>>>>>> updateUserInfo : {}", userDTO.getPassword() );
        mypageMapper.updateUserInfo(userDTO);
    }
}
