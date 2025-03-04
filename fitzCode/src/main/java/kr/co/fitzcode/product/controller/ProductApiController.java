package kr.co.fitzcode.product.controller;

import kr.co.fitzcode.product.dto.CategoryDTO;
import kr.co.fitzcode.product.dto.ProductDTO;
import kr.co.fitzcode.product.mapper.ProductMapper;
import kr.co.fitzcode.product.service.CategoryService;
import kr.co.fitzcode.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<List<ProductDTO>> getProductsByFilter(@RequestParam Map<String, String> filters, Model model) {
//        // Process filters
//        log.info("filters >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> {}", filters);


        // {LOAFERS=on, BOOTS=on, OTHER=on}
        List<ProductDTO> filteredProducts1 = productService.getProductsByFilter(filters);
        return null;
    }



}
