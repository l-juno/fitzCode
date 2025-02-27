package kr.co.fitzcode.admin.controller;

import kr.co.fitzcode.admin.dto.MemberDetailDTO;
import kr.co.fitzcode.admin.service.MembersService;
import kr.co.fitzcode.common.enums.UserTier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/admin/members")
@RequiredArgsConstructor
public class MembersController {
    private final MembersService membersService;

    /**
     * 회원 전체 조회 (페이징)
     * 기본적으로 1페이지, 10개씩 조회 (페이징 가능)
     * @param page 현재 페이지 (기본값: 1)
     * @param size 페이지당 표시할 회원 수 (기본값: 10)
     * @param model 조회된 회원 목록과 페이징 정보를 담을 모델
     * @return 회원 목록 페이지 (admin/member/members)
     */
    @GetMapping
    public String getAllMembers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        Map<String, Object> data = membersService.getAllMembers(page, size);
        model.addAttribute("members", data.get("members")); // 회원 목록
        model.addAttribute("totalCount", data.get("totalCount")); // 전체 회원 수
        model.addAttribute("currentPage", page); // 현재 페이지 번호
        model.addAttribute("pageSize", size); // 페이지 크기

        return "admin/member/members";
    }

    /**
     * 특정 회원 상세 조회
     * @param id 조회할 회원 ID
     * @param model 조회된 회원 정보를 담을 모델
     * @return 회원 상세 페이지 (admin/member/memberDetail)
     */
    @GetMapping("/{id}")
    public String getMemberDetail(@PathVariable int id, Model model) {
        MemberDetailDTO member = membersService.getMemberDetail(id);

        // ENUM 사용하여 등급명과 할인율을 변환
        String tierName = UserTier.getTierName(member.getTierLevel()); // 회원 등급명 (ex: 실버, 골드)
        int discountRate = UserTier.getDiscountRate(member.getTierLevel()); // 회원 할인율

        model.addAttribute("member", member); // 회원 상세 정보
        model.addAttribute("tierName", tierName); // 등급명
        model.addAttribute("discountRate", discountRate); // 할인율

        return "admin/member/memberDetail";
    }

    /**
     * 회원 삭제
     * @param id 삭제할 회원 ID
     * @return 회원 목록 페이지로 리다이렉트
     */
    @PostMapping("/{id}/delete")
    public String deleteMember(@PathVariable int id) {
        membersService.deleteMember(id);
        return "redirect:/admin/members";
    }

    /**
     * 회원 등급 수정
     * @param id 수정할 회원 ID
     * @param tierLevel 변경할 등급 (ex: 1 - 브론즈, 2 - 실버, 3 - 골드, 4 - 플레티넘)
     * @return 회원 상세 페이지로 리다이렉트
     */
    @PostMapping("/{id}/tier")
    public String updateUserTier(@PathVariable int id, @RequestParam int tierLevel) {
        membersService.updateUserTier(id, tierLevel);
        return "redirect:/admin/members/" + id;
    }
}