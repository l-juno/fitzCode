package kr.co.fitzcode.product.controller;

import kr.co.fitzcode.common.service.UserService;
import kr.co.fitzcode.product.dto.CartDTO;
import kr.co.fitzcode.product.dto.ProductResponseDTO;
import kr.co.fitzcode.product.service.CartService;
import kr.co.fitzcode.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
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

    @PostMapping("/addToCart")
    public void addToCart(@RequestParam int productId, @RequestParam int sizeCode) {


        Integer userId = userService.getCurrentUserId();
        if (userId == null) {
            throw new AuthenticationCredentialsNotFoundException("User not authenticated");
        }


        int productSizeId = productService.getProductSizeIdByProductSizeAndCode(productId, sizeCode);

        log.info("--------------------------called addToCart---------------");
        log.info("productId>>>>>>>>>>>>>>>>>>>>: {}", productId);
        log.info("sizeCode>>>>>>>>>>>>>>>>>>>>: {}", sizeCode);
        log.info("productSizeId>>>>>>>>>>>>>>>>>>>>>: {}", productSizeId);
        log.info("userId>>>>>>>>>>>>>>>>>>>>>: {}", userId);

        // Create CartDTO and add to cart using the user's id
        CartDTO cartDTO = CartDTO.builder()
                .userId(userId)
                .productSizeId(productSizeId)
                .productId(productId)
                .build();

        // Add cartDTO to user's cart (this part is not shown in your code)
    }


}
