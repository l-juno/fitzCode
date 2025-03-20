package kr.co.fitzcode.community.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.fitzcode.common.dto.PostDTO;
import kr.co.fitzcode.common.dto.ProductTag;
import kr.co.fitzcode.common.dto.UserDTO;
import kr.co.fitzcode.common.enums.StyleCategory;
import kr.co.fitzcode.community.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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