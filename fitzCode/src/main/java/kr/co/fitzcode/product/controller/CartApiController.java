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

import java.util.List;

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
        // get userid
        int userId = SecurityUtils.getUserId();
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

    @GetMapping("/getCartItems")
    public ResponseEntity<List<CartProductDTO>> getCartItems() {
        int userid = SecurityUtils.getUserId();

        List<CartProductDTO> list =cartService.getCartInformationByUserId(userid);
        log.info("list : {}", list);

        for (CartProductDTO cartProductDTO : list) {
            cartProductDTO.setProductSizes(productService.getAllSizeOfProduct(cartProductDTO.getProductId()));
        }
        return ResponseEntity.ok().body(list);
    }




}
