package kr.co.fitzcode.user.controller;

import kr.co.fitzcode.common.dto.AccountDTO;
import kr.co.fitzcode.common.dto.CouponDTO;
import kr.co.fitzcode.common.dto.OrderDTO;
import kr.co.fitzcode.common.dto.UserDTO;
import kr.co.fitzcode.common.util.SecurityUtils;
import kr.co.fitzcode.user.service.MypageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MypageController {
    private final SecurityUtils securityUtils;
    private final MypageService mypageService;

    // 내 프로필
    @GetMapping("/myInfo")
    public String mypage(Model model) {
        int userId = securityUtils.getUserId();
        UserDTO userDTO = mypageService.getMyInfo(userId);
        List<OrderDTO> orderDTO = mypageService.getOrderList(userId);
        model.addAttribute("userDTO", userDTO);
        model.addAttribute("orderDTO", orderDTO);
        return "user/mypage/myInfo";
    }

    // 프로필 수정 폼으로 이동
    @GetMapping("/updateProfile")
    public String updateProfile(Model model) {
        int userId = securityUtils.getUserId();
        UserDTO userDTO = mypageService.getMyInfo(userId);
        model.addAttribute("dto", userDTO);
        return "user/mypage/updateProfile";
    }

    // 프로필 수정 저장
    @PostMapping("/updateProfile")
    public String updateProfile(@ModelAttribute UserDTO userDTO,
                                @RequestParam(value = "profileImgUrl", required = false) MultipartFile profileImage) {
        mypageService.updateProfile(userDTO, profileImage);
        return "redirect:/mypage/myInfo";
    }

    // 계좌 관리
    @GetMapping("/account")
    public String account(Model model) {
        int userId = securityUtils.getUserId();
        AccountDTO accountDTO = mypageService.getUserAccount(userId);
        model.addAttribute("dto", accountDTO);
        return "user/mypage/account";
    }

    // 수정된 계좌 정보 저장
    @PostMapping("/updateAccount")
    public String updateAccount(@ModelAttribute AccountDTO accountDTO) {
//        log.info("?>>>>>>> accountDTO : {}", accountDTO.getAccountId());
        mypageService.updateAccountData(accountDTO);
        return "redirect:/mypage/account";
    }


    // 회원정보 수정 폼으로 이동
    @GetMapping("/updateInfo")
    public String updateInfo(Model model) {
        int userId = securityUtils.getUserId();
        UserDTO userDTO = mypageService.getMyInfo(userId);
        model.addAttribute("dto", userDTO);
        return "user/mypage/updateInfo";
    }

    // 수정된 회원 정보 저장
    @PostMapping("/updateInfo")
    public String updateInfo(@ModelAttribute UserDTO userDTO) {
//        log.info(">>>>>>>>>>> updateInfo : {}", userDTO.getPassword() );
        mypageService.updateUserInfo(userDTO);
        return "redirect:user/mypage/updateInfo";
    }

    // 사용자 쿠폰
    @GetMapping("/mycoupon")
    public String mycoupon(Model model) {
        int userId = securityUtils.getUserId();
        List<CouponDTO> userCouponList = mypageService.getUserCoupon(userId);
        model.addAttribute("list", userCouponList);
        return "user/mypage/mycoupon";
    }
}
