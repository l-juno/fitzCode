package kr.co.fitzcode.admin.controller;

import kr.co.fitzcode.admin.service.CouponService;
import kr.co.fitzcode.common.dto.CouponDTO;
import kr.co.fitzcode.common.enums.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/coupon")
@RequiredArgsConstructor
@Slf4j
public class AdminCouponController {

    private final CouponService couponService;

    // 관리자 권한 확인 메서드
    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            log.info("비로그인 사용자가 /admin/coupon에 접근 시도 -> /login으로 리다이렉트");
            return false;
        }

        // 소셜 로그인 및 폼 로그인 모두에서 ROLE_ADMIN 확인
        boolean hasAdminRole = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(UserRole.ADMIN.getAuthority()));
        if (!hasAdminRole) {
            log.info("권한 없는 사용자가 /admin/coupon에 접근 시도 -> /로 리다이렉트");
            return false;
        }
        return true;
    }

    // 쿠폰 관리 페이지
    @GetMapping
    public String couponManagement(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
        if (!isAdmin()) {
            return "redirect:/login";
        }

        int pageSize = 10; // 페이지당 10개 쿠폰
        List<CouponDTO> coupons = couponService.getAllCoupons(page, pageSize);
        int totalCoupons = couponService.countAllCoupons();
        int totalPages = (int) Math.ceil((double) totalCoupons / pageSize);

        model.addAttribute("coupons", coupons);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        return "admin/coupon/couponManagement";
    }

    // 쿠폰 추가
    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addCoupon(@RequestBody CouponDTO couponDTO) {
        Map<String, Object> response = new HashMap<>();
        if (!isAdmin()) {
            response.put("success", false);
            response.put("message", "관리자 권한이 필요합니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        try {
            couponDTO.setIsActive(true); // 기본값 설정
            couponDTO.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            couponService.addCoupon(couponDTO);
            response.put("success", true);
            response.put("message", "쿠폰이 성공적으로 추가되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("쿠폰 추가 중 오류 발생: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "쿠폰 추가 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 쿠폰 수정
    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateCoupon(@RequestBody CouponDTO couponDTO) {
        Map<String, Object> response = new HashMap<>();
        if (!isAdmin()) {
            response.put("success", false);
            response.put("message", "관리자 권한이 필요합니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        try {
            couponService.updateCoupon(couponDTO);
            response.put("success", true);
            response.put("message", "쿠폰이 성공적으로 수정되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("쿠폰 수정 중 오류 발생: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "쿠폰 수정 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 쿠폰 삭제
    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteCoupon(@RequestParam("couponId") Integer couponId) {
        Map<String, Object> response = new HashMap<>();
        if (!isAdmin()) {
            response.put("success", false);
            response.put("message", "관리자 권한이 필요합니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        try {
            couponService.deleteCoupon(couponId);
            response.put("success", true);
            response.put("message", "쿠폰이 성공적으로 삭제되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("쿠폰 삭제 중 오류 발생: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "쿠폰 삭제 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 특정 사용자에게 쿠폰 발급
    @PostMapping("/issue/user")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> issueCouponToUser(
            @RequestParam("couponId") Integer couponId,
            @RequestParam("userId") Integer userId,
            @RequestParam("validUntil") String validUntil) {
        Map<String, Object> response = new HashMap<>();
        if (!isAdmin()) {
            response.put("success", false);
            response.put("message", "관리자 권한이 필요합니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        try {
            LocalDate validUntilDate = LocalDate.parse(validUntil); // "YYYY-MM-DD" 형식 예상
            couponService.issueCouponToUser(couponId, userId, java.sql.Timestamp.valueOf(validUntilDate.atStartOfDay()));
            response.put("success", true);
            response.put("message", "쿠폰이 사용자에게 성공적으로 발급되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("쿠폰 발급 중 오류 발생: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "쿠폰 발급 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 전체 사용자에게 쿠폰 발급
    @PostMapping("/issue/all")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> issueCouponToAllUsers(
            @RequestParam("couponId") Integer couponId,
            @RequestParam("validUntil") String validUntil) {
        Map<String, Object> response = new HashMap<>();
        if (!isAdmin()) {
            response.put("success", false);
            response.put("message", "관리자 권한이 필요합니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        try {
            LocalDate validUntilDate = LocalDate.parse(validUntil); // "YYYY-MM-DD" 형식 예상
            couponService.issueCouponToAllUsers(couponId, java.sql.Timestamp.valueOf(validUntilDate.atStartOfDay()));
            response.put("success", true);
            response.put("message", "쿠폰이 전체 사용자에게 성공적으로 발급되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("전체 쿠폰 발급 중 오류 발생: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "쿠폰 발급 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}