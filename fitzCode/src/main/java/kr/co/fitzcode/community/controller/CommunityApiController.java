package kr.co.fitzcode.community.controller;

import kr.co.fitzcode.common.dto.PostLikeDTO;
import kr.co.fitzcode.common.dto.ProductTag;

import kr.co.fitzcode.community.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
public class CommunityApiController {

    private final CommunityService communityService;

    @GetMapping("/search-products")
    public ResponseEntity<List<ProductTag>> searchProductsByName(@RequestParam String productName) {
        List<ProductTag> products = communityService.searchProductsByName(productName);
        return ResponseEntity.ok(products);
    }



}