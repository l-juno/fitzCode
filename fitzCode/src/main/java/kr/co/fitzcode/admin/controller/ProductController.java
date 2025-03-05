package kr.co.fitzcode.admin.controller;

import kr.co.fitzcode.admin.dto.ProductCategoryDTO;
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
    private static final int PAGE_GROUP_SIZE = 5;

    // 상품 목록 조회 (카테고리 필터링, 페이지네이션 포함)
    @GetMapping
    public String listProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) Long parentCategoryId,
            @RequestParam(required = false) Long childCategoryId,
            @RequestParam(defaultValue = "desc") String sort,
            @RequestParam(required = false) String keyword,
            Model model) {

        int pageSize = 10;
        List<ProductDTO> productList;
        int totalProducts;

        if (childCategoryId != null) {
            productList = productService.getProductsByCategory(childCategoryId, page, pageSize, sort, keyword);
            totalProducts = productService.countProductsByCategory(childCategoryId, keyword);
        } else if (parentCategoryId != null) {
            productList = productService.getProductsByParentCategory(parentCategoryId, page, pageSize, sort, keyword);
            totalProducts = productService.countProductsByParentCategory(parentCategoryId, keyword);
        } else {
            productList = productService.getAllProducts(page, pageSize, sort, keyword);
            totalProducts = productService.countAllProducts(keyword);
        }

        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
        int currentGroup = (int) Math.ceil((double) page / PAGE_GROUP_SIZE);
        int startPage = (currentGroup - 1) * PAGE_GROUP_SIZE + 1;
        int endPage = Math.min(startPage + PAGE_GROUP_SIZE - 1, totalPages);

        model.addAttribute("products", productList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("parentCategories", productService.getParentCategories());
        model.addAttribute("parentCategoryId", parentCategoryId);
        model.addAttribute("childCategoryId", childCategoryId);
        model.addAttribute("sort", sort);
        model.addAttribute("keyword", keyword);

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
        return "admin/productDetail";
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

    // 상위 카테고리 조회
    @GetMapping("/categories/parent")
    @ResponseBody
    public List<ProductCategoryDTO> getParentCategories() {
        return productService.getParentCategories();
    }

    // 하위 카테고리 조회
    @GetMapping("/categories/child")
    @ResponseBody
    public List<ProductCategoryDTO> getChildCategories(@RequestParam("parentId") Long parentId) {
        System.out.println("parentId: " + parentId + "에 대한 하위 카테고리 조회");
        return productService.getChildCategories(parentId);
    }
}