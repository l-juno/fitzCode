package kr.co.fitzcode.admin.controller;

import kr.co.fitzcode.admin.dto.ProductDTO;
import kr.co.fitzcode.admin.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    // 상품 목록 조회
    @GetMapping
    public String listProducts(Model model) {
        List<ProductDTO> productList = productService.getAllProducts();
        model.addAttribute("products", productList);
        return "admin/productList";
    }

    // 상품 등록 폼
    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new ProductDTO());
        return "admin/productForm";
    }

    // 상품 등록
    @PostMapping("/add")
    public String addProduct(@ModelAttribute ProductDTO productDTO) {
        productService.addProduct(productDTO);
        return "redirect:/admin/products";
    }

    // 상품 상세 조회
    @GetMapping("/{id}")
    public String getProductDetails(@PathVariable("id") Long productId, Model model) {
        ProductDTO product = productService.getProductById(productId);
        model.addAttribute("product", product);
        return "admin/productDetail"; // Thymeleaf 페이지 연결
    }

    // 상품 수정 폼
    @GetMapping("/edit/{id}")
    public String showEditProductForm(@PathVariable("id") Long productId, Model model) {
        ProductDTO product = productService.getProductById(productId);
        model.addAttribute("product", product);
        return "admin/productForm";
    }

    // 상품 수정
    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable("id") Long productId, @ModelAttribute ProductDTO productDTO) {
        productDTO.setProductId(productId);
        productService.updateProduct(productDTO);
        return "redirect:/admin/products";
    }

    // 상품 삭제
    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long productId) {
        productService.deleteProduct(productId);
        return "redirect:/admin/products";
    }
}