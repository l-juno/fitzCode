package kr.co.fitzcode.product.controller;

import kr.co.fitzcode.product.dto.*;
import kr.co.fitzcode.product.service.CategoryService;
import kr.co.fitzcode.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductApiController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping(value = "/list/{pageNum}")
    public ResponseEntity<List<ProductDTO>> list(@PathVariable Integer pageNum) {
        List<ProductDTO> list = productService.getProductsByPage(pageNum);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/category")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> list = categoryService.getAllCategories();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping("/getProductsByFilter")
    public ResponseEntity<ProductResponseDTO> getProductsByFilter(@RequestParam MultiValueMap<String, String> filters, Model model) {

        // Extract the category codes into a list
        List<String> codes = filters.keySet().stream()
                .filter(key -> !key.equals("page") && !key.equals("searchText"))
                .collect(Collectors.toList());

        log.info("codes type: {}", codes.getClass().getName());

        // Extract the search text (single value)
        String searchText = filters.getFirst("searchText");
        String currentPageString = filters.getFirst("page");
        int currentPage = Integer.parseInt(currentPageString);
        log.info("searchText: {}, currentPageString: {}, int page: {}", searchText, currentPageString,currentPage);
        log.info("codes: {}", codes);

        List<ProductDTO> filteredProducts;
        int totalLength;
        if ((codes == null || codes.isEmpty()) && (searchText == null || searchText.isEmpty())) {
            filteredProducts = productService.getProductsByPage(currentPage);
            totalLength = productService.getCountOfAllProducts();

        } else {
            filteredProducts = productService.getProductsByFilterAndPage(codes, searchText, currentPage);
            totalLength = productService.getProductsCountByFilter(codes, searchText);
        }
        ProductResponseDTO productResponseDTO = ProductResponseDTO.builder()
                .list(filteredProducts)
                .totalLength(totalLength)
                .currentPage(currentPage)
                .build();

        return ResponseEntity.ok().body(productResponseDTO);
    }


    @PostMapping("/getProductSizes")
    public ResponseEntity<List<ProductSizeDTO>> getProductSizes(@RequestParam int productId) {
        List<ProductSizeDTO> listSize = productService.getAllSizeOfProduct(productId);
        return ResponseEntity.ok().body(listSize);
    }





}
