package kr.co.fitzcode.admin.controller;

import kr.co.fitzcode.admin.dto.ProductDetailDTO;
import kr.co.fitzcode.admin.service.ProductDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class ProductDetailController {

    private final ProductDetailService productDetailService;

    // 상품 상세 정보 페이지
    @GetMapping("/{productId}")
    public String getProductDetail(@PathVariable Long productId, Model model) {
        ProductDetailDTO productDetail = productDetailService.getProductDetail(productId);

        if (productDetail == null) {
            throw new RuntimeException("상품을 찾을 수 없습니다. ID: " + productId);
        }

        model.addAttribute("productDetail", productDetail);
        return "admin/productDetail";
    }

    // 할인 가격 + 사이즈 재고 수정
    @PostMapping("/{productId}/update")
    public String updateProduct(
            @PathVariable Long productId,
            @ModelAttribute("productDetail") ProductDetailDTO productDetail
    ) {
        // 할인가 업데이트
        if (productDetail.getDiscountedPrice() != null) {
            productDetailService.updateDiscountedPrice(productId, productDetail.getDiscountedPrice());
        }

        // 사이즈 재고 업데이트 (인서트 또는 업데이트)
        if (productDetail.getSizes() != null && !productDetail.getSizes().isEmpty()) {
            productDetailService.updateSizes(productId, productDetail.getSizes());
        }
        return "redirect:/admin/products/" + productId;
    }

    // 상품 상태 업데이트
    @PostMapping("/{productId}/update-status")
    @ResponseBody
    public String updateStatus(@PathVariable Long productId, @RequestBody Map<String, Integer> request) {
        Integer newStatus = request.get("status");
        if (newStatus == null || (newStatus != 1 && newStatus != 2 && newStatus != 3)) {
            return "잘못된 상태 값입니다.";
        }
        productDetailService.updateStatus(productId, newStatus);
        return "success";
    }

}