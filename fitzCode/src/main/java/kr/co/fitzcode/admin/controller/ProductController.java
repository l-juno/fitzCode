package kr.co.fitzcode.admin.controller;

import kr.co.fitzcode.admin.dto.ProductCategoryDTO;
import kr.co.fitzcode.admin.dto.ProductDTO;
import kr.co.fitzcode.admin.service.ProductService;
import kr.co.fitzcode.common.enums.ProductSize;
import kr.co.fitzcode.common.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        model.addAttribute("parentCategories", productService.getParentCategories());
        return "admin/productForm";
    }

    // 상품 등록
    @PostMapping("/add")
    public String addProduct(
            @ModelAttribute ProductDTO productDTO,
            BindingResult result,
            @RequestParam("mainImageFile") MultipartFile mainImageFile,
            @RequestParam("additionalImageFiles") List<MultipartFile> additionalImageFiles) {
        if (result.hasErrors()) {
            return "admin/productForm";
        }

        productService.addProduct(productDTO, mainImageFile, additionalImageFiles);
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

    // 상위 카테고리 사이즈 조회
    @GetMapping("/sizes")
    @ResponseBody
    public List<Map<String, Object>> getSizesByParentCategory(@RequestParam("parentId") Long parentId) {
        if (parentId == 1) { // 신발
            return Arrays.stream(ProductSize.values())
                    .filter(size -> size.getCode() <= 9)
                    .map(size -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("code", size.getCode());
                        map.put("description", size.getDescription());
                        return map;
                    })
                    .collect(Collectors.toList());
        } else if (parentId == 2 || parentId == 3) { // 상의 또는 하의
            return Arrays.stream(ProductSize.values())
                    .filter(size -> size.getCode() >= 10)
                    .map(size -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("code", size.getCode());
                        map.put("description", size.getDescription());
                        return map;
                    })
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}