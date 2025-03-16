package kr.co.fitzcode.community.controller;

import kr.co.fitzcode.common.dto.ProductDTO;
import kr.co.fitzcode.community.service.CommunityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
public class CommunityApiController {

    private final CommunityService communityService;

    // 상품 이름으로 검색하여 상품 리스트 반환
    @GetMapping("/search-products")
    public List<ProductDTO> searchProductsByName(
            @RequestParam String productName,
            @RequestParam(defaultValue = "0") int offset) {
        // 상품 이름과 오프셋을 받아서 검색
        List<ProductDTO> products = communityService.searchProductsByName(productName, offset);
        return products;
    }
}