package kr.co.fitzcode.product.controller;

import kr.co.fitzcode.product.dto.ProductDTO;
import kr.co.fitzcode.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductApiController {

    private final ProductService productService;

    @GetMapping(value = "/list/{pageNum}")
    public ResponseEntity<List<ProductDTO>> list(@PathVariable Integer pageNum) {
        List<ProductDTO> list = productService.getProductsByPage(pageNum);
        return ResponseEntity.ok().body(list);
    }
}
