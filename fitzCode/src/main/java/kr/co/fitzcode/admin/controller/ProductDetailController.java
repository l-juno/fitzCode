package kr.co.fitzcode.admin.controller;

import kr.co.fitzcode.admin.dto.ProductDetailDTO;
import kr.co.fitzcode.admin.dto.QnaDTO;
import kr.co.fitzcode.admin.service.ProductDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        if (productDetail.getDiscountedPrice() != null) {
            productDetailService.updateDiscountedPrice(productId, productDetail.getDiscountedPrice());
        }
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

    @PostMapping("/{productId}/delete")
    public String deleteProduct(@PathVariable Long productId, RedirectAttributes redirectAttributes) {
        try {
            productDetailService.deleteProduct(productId);
            redirectAttributes.addFlashAttribute("successMessage", "상품이 삭제되었습니다.");
            return "redirect:/admin/products";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "상품 삭제 실패: " + e.getMessage());
            return "redirect:/admin/products";
        }
    }

    // 리뷰 관리 페이지
    @GetMapping("/review/{productId}")
    public String getReviewPage(@PathVariable Long productId, Model model) {
        model.addAttribute("reviews", productDetailService.getReviewsByProductId(productId));
        model.addAttribute("productId", productId);
        return "admin/reviewManagement";
    }

    // 리뷰 삭제
    @PostMapping("/review/delete/{reviewId}")
    @ResponseBody
    public String deleteReview(@PathVariable Long reviewId) {
        try {
            productDetailService.deleteReview(reviewId);
            return "success";
        } catch (Exception e) {
            return "리뷰 삭제 실패: " + e.getMessage();
        }
    }

    // Q&A 관리 페이지
    @GetMapping("/qna/{productId}")
    public String getQnaPage(@PathVariable Long productId, Model model) {
        model.addAttribute("qnas", productDetailService.getQnasByProductId(productId));
        model.addAttribute("productId", productId);
        return "admin/qnaManagement";
    }

    // Q&A 답변 추가
    @PostMapping("/qna/{productId}/answer")
    @ResponseBody
    public String addQnaAnswer(@PathVariable Long productId, @RequestBody QnaDTO qnaDTO) {
        try {
            productDetailService.addQnaAnswer(qnaDTO.getQnaId(), qnaDTO.getAnswer());
            return "success";
        } catch (Exception e) {
            return "답변 추가 실패: " + e.getMessage();
        }
    }

    // Q&A 답변 수정
    @PostMapping("/qna/{productId}/update-answer")
    @ResponseBody
    public String updateQnaAnswer(@PathVariable Long productId, @RequestBody QnaDTO qnaDTO) {
        try {
            productDetailService.updateQnaAnswer(qnaDTO.getQnaId(), qnaDTO.getAnswer());
            return "success";
        } catch (Exception e) {
            return "답변 수정 실패: " + e.getMessage();
        }
    }

    // Q&A 삭제
    @PostMapping("/qna/delete/{qnaId}")
    @ResponseBody
    public String deleteQna(@PathVariable Long qnaId) {
        try {
            productDetailService.deleteQna(qnaId);
            return "success";
        } catch (Exception e) {
            return "Q&A 삭제 실패: " + e.getMessage();
        }
    }
}