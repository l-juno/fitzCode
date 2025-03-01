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

    @GetMapping("/{id}")
    public String getMemberDetail(@PathVariable int id, Model model) {
        MemberDetailDTO member = membersService.getMemberDetail(id);

        // ENUM 사용하여 등급명과 할인율을 변환
        String tierName = UserTier.getTierName(member.getTierLevel()); // 회원 등급명
        int discountRate = UserTier.getDiscountRate(member.getTierLevel()); // 회원 할인율

        model.addAttribute("member", member); // 회원 상세 정보
        model.addAttribute("tierName", tierName); // 등급명
        model.addAttribute("discountRate", discountRate); // 할인율

        return "admin/member/memberDetail";
    }

    @PostMapping("/{id}/delete")
    public String deleteMember(@PathVariable int id) {
        membersService.deleteMember(id);
        return "redirect:/admin/members";
    }

    @PostMapping("/{id}/tier")
    public String updateUserTier(@PathVariable int id, @RequestParam int tierLevel) {
        membersService.updateUserTier(id, tierLevel);
        return "redirect:/admin/members/" + id;
    }
}