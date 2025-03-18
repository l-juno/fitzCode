package kr.co.fitzcode.order.controller;


import kr.co.fitzcode.common.dto.CouponDTO;
import kr.co.fitzcode.common.util.SecurityUtils;
import kr.co.fitzcode.order.service.CouponService;
import kr.co.fitzcode.order.service.PriceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
@Slf4j
public class CouponController {

    private final CouponService couponService;
    private final PriceService priceService;

    @GetMapping("/getUsersCoupons")
    public ResponseEntity<List<CouponDTO>> getUsersCoupons() {
        // get valid couponId from user_coupon table then get the coupons by the id

        int userId = SecurityUtils.getUserId();
        List<CouponDTO> coupons = couponService.getUsersValidCoupon(userId);
        log.info("getUsersCoupons: {}", coupons);


        return ResponseEntity.ok(coupons);
    }

    @GetMapping("getPriceWithCoupon")
    public ResponseEntity<Integer> getPriceWithCoupon(@RequestParam("couponId") int couponId,
                                                      @RequestParam("productId") int productId) {
        log.info("getPriceWithCoupon couponId: {}", couponId);
        log.info("getPriceWithCoupon productId: {}", productId);

        int finalPrice = priceService.calculatePriceByProductId(productId, couponId);
        log.info("final price: {}", finalPrice);

        return ResponseEntity.ok(finalPrice);

    }
}
