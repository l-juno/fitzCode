package kr.co.fitzcode.product.controller;

import kr.co.fitzcode.common.dto.CartDTO;
import kr.co.fitzcode.common.dto.CartProductDTO;
import kr.co.fitzcode.common.service.CustomUserDetails;
import kr.co.fitzcode.common.service.UserService;
import kr.co.fitzcode.common.util.SecurityUtils;
import kr.co.fitzcode.product.service.CartService;
import kr.co.fitzcode.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Slf4j
public class CartApiController {

    private final CartService cartService;
    private final ProductService productService;
    private final UserService userService;
    private final SecurityUtils securityUtils;

    @GetMapping("/count")
    public ResponseEntity<Map<String, Integer>> getCartCount(Principal principal) {
        log.info("GET /api/cart/count 요청 처리");
        if (principal == null) {
            log.warn("Principal이 null입니다 - 인증되지 않은 접근");
            Map<String, Integer> response = new HashMap<>();
            response.put("count", 0); // 비인증 사용자는 0 return
            return ResponseEntity.ok(response);
        }

        // TODO 여기서 실제 카트 수량을 계산하는 로직추가해줘야함
        int cartCount = 0;
        Map<String, Integer> response = new HashMap<>();
        response.put("count", cartCount);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/addToCart")
    public void addToCart(@RequestParam int productId, @RequestParam int sizeCode) {
        // get userid
        int userId = securityUtils.getUserId();
        int productSizeId = productService.getProductSizeIdByProductSizeAndCode(productId, sizeCode);
        log.info("productId>>>>>>>>>>>>>>>>>>>>: {}", productId);
        log.info("sizeCode>>>>>>>>>>>>>>>>>>>>: {}", sizeCode);
        log.info("productSizeId>>>>>>>>>>>>>>>>>>>>>: {}", productSizeId);
        log.info("userId>>>>>>>>>>>>>>>>>>>>>>>>>: {}", userId);


        CartDTO cartDTO = CartDTO.builder()
                .userId(userId)
                .productId(productId)
                .quantity(1)
                .productSizeId(productSizeId)
                .build();
        cartService.addProductToCart(cartDTO);

    }

    @GetMapping("/getCartItems")
    public ResponseEntity<List<CartProductDTO>> getCartItems() {
        int userid = SecurityUtils.getUserId();
        List<CartProductDTO> list = cartService.getCartInformationByUserId(userid);
        log.info("cart list : {}", list);

        for (CartProductDTO cartProductDTO : list) {
            cartProductDTO.setProductSizes(productService.getAllSizeOfProduct(cartProductDTO.getProductId()));
        }
        return ResponseEntity.ok().body(list);
    }


}
