package kr.co.fitzcode.product.controller;

import kr.co.fitzcode.product.dto.ProductDTO;
import kr.co.fitzcode.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping(value = {"/list/{pageNum}", "/list"})
    public String list(@PathVariable(required = false) Integer pageNum, Model model) {
        if (pageNum == null) {
            pageNum = 1;
        }
        List<ProductDTO> productList = productService.getProductsByPage(pageNum);
        model.addAttribute("productList", productList);
        return "product/productList";
    }

}
