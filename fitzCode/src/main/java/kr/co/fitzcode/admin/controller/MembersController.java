package kr.co.fitzcode.admin.controller;

import kr.co.fitzcode.admin.dto.MemberDetailDTO;
import kr.co.fitzcode.admin.service.MembersService;
import kr.co.fitzcode.common.enums.UserRole;
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

    // 필터링된 회원 목록 조회
    @GetMapping
    public String getFilteredMembers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer role, // 역할 필터
            @RequestParam(defaultValue = "recent") String sortOrder, // 정렬 기준
            Model model) {

        // 필터링된 회원 목록 및 총 개수 조회
        Map<String, Object> data = membersService.getFilteredMembers(page, size, role, sortOrder);

        model.addAttribute("members", data.get("members"));
        model.addAttribute("totalCount", data.get("totalCount"));
        model.addAttribute("currentPage", data.get("currentPage"));
        model.addAttribute("pageSize", data.get("pageSize"));
        model.addAttribute("role", role); // 선택된 역할 ID
        model.addAttribute("sortOrder", sortOrder); // 정렬 기준

        return "admin/member/members";
    }

    // 특정 회원 상세 정보 조회
    @GetMapping("/{id}")
    public String getMemberDetail(@PathVariable int id, Model model) {
        MemberDetailDTO member = membersService.getMemberDetail(id);

        // ENUM 사용하여 등급명 | 할인율 변환
        String tierName = UserTier.getTierName(member.getTierLevel()); // 회원 등급명
        int discountRate = UserTier.getDiscountRate(member.getTierLevel()); // 회원 할인율

        model.addAttribute("member", member); // 회원 상세 정보
        model.addAttribute("tierName", tierName); // 등급명
        model.addAttribute("discountRate", discountRate); // 할인율

        return "admin/member/memberDetail";
    }

    // 특정 회원 삭제
    @PostMapping("/{id}/delete")
    public String deleteMember(@PathVariable int id) {
        membersService.deleteMember(id);
        return "redirect:/admin/members";
    }

    // 특정 회원의 등급 업데이트
    @PostMapping("/{id}/tier")
    public String updateUserTier(@PathVariable int id, @RequestParam int tierLevel) {
        membersService.updateUserTier(id, tierLevel);
        return "redirect:/admin/members/" + id;
    }
}