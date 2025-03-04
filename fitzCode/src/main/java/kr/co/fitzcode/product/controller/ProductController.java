package kr.co.fitzcode.product.controller;

import ch.qos.logback.classic.Logger;
import groovy.util.logging.Slf4j;
import kr.co.fitzcode.product.dto.ProductDTO;
import kr.co.fitzcode.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.XSlf4j;
import org.slf4j.LoggerFactory;
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

    @GetMapping(value = { "/list", "/list/{pageNum}"})
    public String list(@PathVariable(required = false) Integer pageNum, Model model) {
//        // paging
//        int currentPage = pageNum == null ? 1 : pageNum;
//        int firstPage = 1;
//        int lastPage = productService.getNumberOfPages();
//        Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
//        logger.info("Last page number: {}", lastPage);
//        if (currentPage <= 3) {
//            lastPage = 5;
//        } else if (currentPage + 2 >= lastPage) {
//            firstPage = lastPage - 4;
//        } else {
//            firstPage = currentPage - 2;
//            lastPage = currentPage + 2;
//        }
//        model.addAttribute("firstPage", firstPage);
//        model.addAttribute("lastPage", lastPage);
//        model.addAttribute("currentPage", currentPage);
//
//        // getting list
//        List<ProductDTO> list = productService.getProductsByPage(currentPage);
//        model.addAttribute("productList", list);
        return "product/productList";
    }


    @GetMapping("/detail/{productId}")
    public String detail(@PathVariable int productId, Model model) {
        ProductDTO product = productService.getProductById(productId);
        model.addAttribute("product", product);
        return "product/productDetail";
    }

    @GetMapping("/filter")
    public String filter(Model model) {
        return "product/filter";
    }


}
