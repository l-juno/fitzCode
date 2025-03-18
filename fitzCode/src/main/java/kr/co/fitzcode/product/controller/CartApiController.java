package kr.co.fitzcode.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.fitzcode.common.dto.CartDTO;
import kr.co.fitzcode.common.dto.CategoryDTO;
import kr.co.fitzcode.common.service.UserService;
import kr.co.fitzcode.common.util.SecurityUtils;
import kr.co.fitzcode.product.service.CartService;
import kr.co.fitzcode.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Cart API", description = "장바구니 관련 API 제공, 장바구니 수량 조회, 상품 추가")
public class CartApiController {

    private final CartService cartService;
    private final ProductService productService;
    private final UserService userService;
    private final SecurityUtils securityUtils;

    @Operation(summary = "장바구니 수량 조회", description = "현재 사용자의 장바구니 상품 수량 조회")
    @GetMapping("/count")
    public ResponseEntity<Map<String, Integer>> getCartCount(Principal principal) {
        log.info("GET /api/cart/count 요청 처리");
        if (principal == null) {
            log.warn("Principal이 null입니다 - 인증되지 않은 접근");
            Map<String, Integer> response = new HashMap<>();
            response.put("count", 0);
            return ResponseEntity.ok(response);
        }

        int userId = securityUtils.getUserIdFromPrincipal(principal);
        int cartCount = cartService.getCartCount(userId);
        Map<String, Integer> response = new HashMap<>();
        response.put("count", cartCount);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "장바구니에 상품 추가", description = "제품 ID와 사이즈 코드로 장바구니에 상품 추가")
    @PostMapping("/add")
    public ResponseEntity<Map<String, Integer>> addToCart(
            @Parameter(description = "제품 ID", example = "1") @RequestParam int productId,
            @Parameter(description = "사이즈 코드", example = "1") @RequestParam int sizeCode,
            Principal principal) {
        if (principal == null) {
            log.warn("Principal이 null입니다 - 인증되지 않은 접근");
            Map<String, Integer> response = new HashMap<>();
            response.put("count", 0);
            response.put("success", 0);
            return ResponseEntity.badRequest().body(response);
        }

        int userId = securityUtils.getUserIdFromPrincipal(principal);
        int productSizeId = productService.getProductSizeIdByProductSizeAndCode(productId, sizeCode);
        log.info("productId: {}", productId);
        log.info("sizeCode: {}", sizeCode);
        log.info("productSizeId: {}", productSizeId);
        log.info("userId: {}", userId);

        CartDTO cartDTO = CartDTO.builder()
                .userId(userId)
                .productId(productId)
                .quantity(1)
                .productSizeId(productSizeId)
                .build();
        cartService.addProductToCart(cartDTO);

        // 장바구니 수량 반환
        int cartCount = cartService.getCartCount(userId);
        Map<String, Integer> response = new HashMap<>();
        response.put("count", cartCount);
        response.put("success", 1);
        return ResponseEntity.ok(response);
    }
}