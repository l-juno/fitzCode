package kr.co.fitzcode.product.controller;

import kr.co.fitzcode.common.dto.ProductDTO;
import kr.co.fitzcode.common.dto.ProductImageDTO;
import kr.co.fitzcode.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping(value = { "/list", "/list/{pageNum}" })
    public String list(@PathVariable(required = false) Integer pageNum, Model model) {
        return "product/productList";
    }

    @GetMapping("/detail/{productId}")
    public String detail(@PathVariable int productId, Model model) {
        ProductDTO product = productService.getProductById(productId);
        model.addAttribute("product", product);

        List<ProductImageDTO> productImageList = productService.getProductImagesByProductId(productId);
        model.addAttribute("productImageList", productImageList);

        // categoryId가 제대로 전달되는지 로그로 확인
        log.info("Product ID: {}, Category ID: {}", product.getProductId(), product.getCategoryId());

        return "product/productDetail";
    }

    @GetMapping("/filter")
    public String filter(Model model) {
        return "product/filter";
    }
}