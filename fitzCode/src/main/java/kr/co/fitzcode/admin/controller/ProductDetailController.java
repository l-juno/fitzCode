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

        // 카테고리 확인 (상위 및 하위 카테고리 포함)
        Integer categoryId = productDetail.getCategoryId();
        boolean isShoeCategory = isShoeCategory(categoryId);
        boolean isClothingCategory = isClothingCategory(categoryId);

        List<ProductSizeDTO> allSizes = getAllSizes(isShoeCategory, isClothingCategory, productDetail.getSizes());
        productDetail.setAllSizes(allSizes);
        productDetail.setSizes(new ArrayList<>(allSizes)); // sizes를 allSizes로 초기화

        model.addAttribute("productDetail", productDetail);
        return "admin/productDetail";
    }

    // 신발 카테고리 여부 확인
    private boolean isShoeCategory(Integer categoryId) {
        if (categoryId == null) return false;
        // 상위 카테고리 (category_id = 1) 및 하위 카테고리 (4, 5, 6, 10, 11, 12)
        return categoryId == 1 || categoryId == 4 || categoryId == 5 || categoryId == 6 ||
                categoryId == 10 || categoryId == 11 || categoryId == 12;
    }

    // 의류 카테고리 여부 확인
    private boolean isClothingCategory(Integer categoryId) {
        if (categoryId == null) return false;
        // 상의 (category_id = 2) 및 하위 카테고리 (7, 8, 9)
        // 하의 (category_id = 3) 및 하위 카테고리 (14, 15, 16)
        return (categoryId == 2 || categoryId == 7 || categoryId == 8 || categoryId == 9) ||
                (categoryId == 3 || categoryId == 14 || categoryId == 15 || categoryId == 16);
    }

    // 모든 사이즈를 가져오는 메서드 (카테고리별 필터링)
    private List<ProductSizeDTO> getAllSizes(boolean isShoeCategory, boolean isClothingCategory, List<ProductSizeDTO> existingSizes) {
        List<ProductSizeDTO> allSizes = new ArrayList<>();
        for (ProductSize size : ProductSize.values()) {
            // 신발 카테고리: 신발 사이즈(1~9)만 포함
            if (isShoeCategory && size.getCode() <= 9) {
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
            // 의류 카테고리: 의류 사이즈(10~15)만 포함
            else if (isClothingCategory && size.getCode() >= 10) {
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
        System.out.println("isShoeCategory: " + isShoeCategory + ", isClothingCategory: " + isClothingCategory + ", allSizes size: " + allSizes.size());
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