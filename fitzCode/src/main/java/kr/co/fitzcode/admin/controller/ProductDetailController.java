package kr.co.fitzcode.admin.controller;

import kr.co.fitzcode.admin.dto.ProductDetailDTO;
import kr.co.fitzcode.admin.dto.ProductSizeDTO;
import kr.co.fitzcode.admin.service.ProductDetailService;
import kr.co.fitzcode.common.enums.ProductSize;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

        // 신발 카테고리인지 확인 (categoryId가 신발 카테고리 ID와 일치하는지 가정)
        boolean isShoeCategory = productDetail.getCategoryId() != null && productDetail.getCategoryId() == 1; // 예: 1은 신발 카테고리 ID
        List<ProductSizeDTO> allSizes = getAllSizes(isShoeCategory, productDetail.getSizes());
        productDetail.setAllSizes(allSizes);
        productDetail.setSizes(new ArrayList<>(allSizes)); // sizes를 allSizes로 초기화

        model.addAttribute("productDetail", productDetail);
        return "admin/productDetail";
    }

    // 모든 사이즈를 가져오는 메서드 (기존 사이즈와 병합)
    private List<ProductSizeDTO> getAllSizes(boolean isShoeCategory, List<ProductSizeDTO> existingSizes) {
        List<ProductSizeDTO> allSizes = new ArrayList<>();
        for (ProductSize size : ProductSize.values()) {
            if (!isShoeCategory || (isShoeCategory && size.getCode() <= 9)) { // 신발 사이즈는 1~9
                ProductSizeDTO dto = new ProductSizeDTO();
                dto.setSizeCode(size.getCode());
                // 기존 사이즈 데이터가 있으면 재고 반영
                if (existingSizes != null) {
                    for (ProductSizeDTO existing : existingSizes) {
                        if (existing.getSizeCode() != null && existing.getSizeCode().equals(size.getCode())) {
                            dto.setProductSizeId(existing.getProductSizeId());
                            dto.setStock(existing.getStock() != null ? existing.getStock() : 0);
                            break;
                        }
                    }
                }
                if (dto.getStock() == null) dto.setStock(0); // 데이터 없으면 0
                allSizes.add(dto);
            }
        }
        return allSizes;
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
}