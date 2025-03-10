package kr.co.fitzcode.product.controller;

import kr.co.fitzcode.common.dto.CartDTO;
import kr.co.fitzcode.common.service.CustomUserDetails;
import kr.co.fitzcode.common.service.UserService;
import kr.co.fitzcode.common.util.SecurityUtils;
import kr.co.fitzcode.product.service.CartService;
import kr.co.fitzcode.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Slf4j
public class CartApiController {

    private final CartService cartService;
    private final ProductService productService;
    private final UserService userService;
    private final SecurityUtils securityUtils;

    @PostMapping("/addToCart")
    public void addToCart(@RequestParam int productId, @RequestParam int sizeCode) {
        // get userid
        int userId = securityUtils.getUserId();
        int productSizeId = productService.getProductSizeIdByProductSizeAndCode(productId, sizeCode);
        log.info("--------------------------called addToCart---------------");
        log.info("productId>>>>>>>>>>>>>>>>>>>>: {}", productId);
        log.info("sizeCode>>>>>>>>>>>>>>>>>>>>: {}", sizeCode);
        log.info("productSizeId>>>>>>>>>>>>>>>>>>>>>: {}", productSizeId);
        log.info("userId>>>>>>>>>>>>>>>>>>>>>>>>>: {}", userId);


        // Create CartDTO and add to cart using the user's id
        CartDTO cartDTO = CartDTO.builder()
                .userId(userId)
                .productId(productId)
                .quantity(1)
                .productSizeId(productSizeId)
                .build();
        cartService.addProductToCart(cartDTO);

    }


}
