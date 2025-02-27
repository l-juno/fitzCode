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

    // 회원 전체 조회 (페이징)
    @GetMapping
    public String getAllMembers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        Map<String, Object> data = membersService.getAllMembers(page, size);
        model.addAttribute("members", data.get("members"));
        model.addAttribute("totalCount", data.get("totalCount"));
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);

        return "admin/member/members";
    }

    // 회원 상세 조회
    @GetMapping("/{id}")
    public String getMemberDetail(@PathVariable int id, Model model) {
        MemberDetailDTO member = membersService.getMemberDetail(id);

        // ENUM 사용하여 등급명과 할인율을 변환
        String tierName = UserTier.getTierName(member.getTierLevel());
        int discountRate = UserTier.getDiscountRate(member.getTierLevel());

        model.addAttribute("member", member);
        model.addAttribute("tierName", tierName);
        model.addAttribute("discountRate", discountRate);

        return "admin/member/memberDetail";
    }
    // 회원 삭제
    @PostMapping("/{id}/delete")
    public String deleteMember(@PathVariable int id) {
        membersService.deleteMember(id);
        return "redirect:/admin/members";
    }

    // 회원 등급 수정
    @PostMapping("/{id}/tier")
    public String updateUserTier(@PathVariable int id, @RequestParam int tierLevel) {
        membersService.updateUserTier(id, tierLevel);
        return "redirect:/admin/members/" + id;
    }
}