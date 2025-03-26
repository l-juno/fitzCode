package kr.co.fitzcode.community.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.fitzcode.common.dto.*;
import kr.co.fitzcode.community.service.CommunityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/community")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    @GetMapping("/list")
    public String list(Model model, HttpSession session, HttpServletRequest request) {
        UserDTO user = (UserDTO) session.getAttribute("dto");
        List<PostDTO> posts = communityService.getAllPosts();
        model.addAttribute("username", user.getUserName());
        model.addAttribute("profileImage", user.getProfileImage());
        model.addAttribute("posts", posts);
        return "community/communityList";
    }

    @GetMapping("/form")
    public String form() {
        return "community/communityForm";
    }

    @PostMapping("/writeForm")
    public String createPost(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("images") List<MultipartFile> images,
            @RequestParam("productIds") String productIds,
            @RequestParam("styleCategory") String styleCategory,
            HttpSession session,
            RedirectAttributes redirectAttributes) throws IOException {

        UserDTO userDTO = (UserDTO) session.getAttribute("dto");
        if (userDTO == null) {
            throw new IllegalStateException("사용자 정보가 세션에 없음");
        }
        int userId = userDTO.getUserId();

        List<Long> productIdList = productIds != null && !productIds.isEmpty()
                ? Arrays.stream(productIds.split(","))
                .filter(s -> !s.isEmpty())
                .map(Long::parseLong)
                .toList()
                : null;

        String styleCategoryValue = styleCategory.isEmpty() ? "" : styleCategory;

        PostDTO postDTO = PostDTO.builder()
                .userId(userId)
                .styleCategory(styleCategoryValue)
                .title(title)
                .content(content)
                .build();

        int postId = communityService.insertPost(postDTO, productIdList, images);

        redirectAttributes.addAttribute("postId", postId);
        return "redirect:/community/detail/{postId}";
    }

    @GetMapping("/detail/{postId}")
    public String getPostDetail(@PathVariable("postId") int postId, Model model, HttpSession session) {
        UserDTO userDTO = (UserDTO) session.getAttribute("dto");
        if (userDTO == null) {
            throw new IllegalStateException("사용자 정보가 세션에 없음");
        }

        PostDTO postDetail = communityService.getPostDetail(postId);
        List<ProductTag> productTags = communityService.getProductTagsByPostId(postId);
        List<PostDTO> otherStyles = communityService.getOtherStylesByUserId(postDetail.getUserId(), postId);
        List<PostImageDTO> postImages = communityService.getPostImagesByPostId(postId);

        model.addAttribute("post", postDetail);
        model.addAttribute("productTags", productTags);
        model.addAttribute("otherStyles", otherStyles);
        model.addAttribute("postImages", postImages);
        model.addAttribute("currentUser", userDTO);
        model.addAttribute("username", userDTO.getUserName());
        model.addAttribute("profileImage", userDTO.getProfileImage());

        return "community/communityDetail";
    }

    @GetMapping("/modify/{id}")
    public String showModifyForm(@PathVariable("id") int postId, Model model) {
        PostDTO post = communityService.getPostById(postId);
        List<PostImageDTO> postImages = communityService.getPostImagesByPostId(postId);
        List<ProductTag> productTags = communityService.getProductTagsByPostId(postId);

        model.addAttribute("post", post);
        model.addAttribute("postImages", postImages);
        model.addAttribute("productTags", productTags);
        return "community/communityModify";
    }

    @PostMapping("/modify/{id}")
    public String modifyPost(
            @PathVariable("id") int id,
            @ModelAttribute("post") PostDTO postDTO, // 폼 데이터를 PostDTO로 바인딩
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            @RequestParam(value = "productIds", required = false) String productIds,
            HttpSession session,
            RedirectAttributes redirectAttributes) throws IOException {

        UserDTO userDTO = (UserDTO) session.getAttribute("dto");
        if (userDTO == null) {
            throw new IllegalStateException("사용자 정보가 세션에 없음");
        }

        PostDTO existingPost = communityService.getPostById(id);
        if (existingPost.getUserId() != userDTO.getUserId()) {
            throw new IllegalStateException("수정 권한이 없습니다.");
        }

        // PostDTO에 postId 설정
        postDTO.setPostId(id);
        postDTO.setUserId(existingPost.getUserId()); // 기존 사용자 ID 유지

        // productIds를 List<Long>으로 변환
        List<Long> productIdList = productIds != null && !productIds.isEmpty()
                ? Arrays.stream(productIds.split(","))
                .filter(s -> !s.isEmpty())
                .map(Long::parseLong)
                .toList()
                : null;

        // 서비스 호출
        communityService.updatePost(postDTO, productIdList, images);

        redirectAttributes.addAttribute("postId", id);
        return "redirect:/community/detail/{postId}";
    }
}