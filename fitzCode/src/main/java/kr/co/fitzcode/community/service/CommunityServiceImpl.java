package kr.co.fitzcode.community.service;

import kr.co.fitzcode.common.dto.PostDTO;
import kr.co.fitzcode.common.dto.PostImageDTO;
import kr.co.fitzcode.common.dto.ProductTag;
import kr.co.fitzcode.community.mapper.CommunityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommunityServiceImpl implements CommunityService {

    private final CommunityMapper communityMapper;

    @Value("${file.upload-dir:./uploads}")
    private String uploadDir;

    @Override
    public List<ProductTag> searchProductsByName(String productName) {
        List<ProductTag> products = communityMapper.searchProductsByName(productName);
        return products != null ? products : Collections.emptyList();
    }

    @Override
    public int insertPost(PostDTO postDTO, List<Long> productIdList, List<MultipartFile> images) throws IOException {
        List<String> imageUrls = new ArrayList<>();

        if (images != null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                String imageUrl = uploadImage(image);
                imageUrls.add(imageUrl);
            }
            postDTO.setThumbnailImageUrl(imageUrls.get(0));
        }

        return insertPostInTransaction(postDTO, productIdList, imageUrls);
    }

    @Transactional
    int insertPostInTransaction(PostDTO postDTO, List<Long> productIdList, List<String> imageUrls) {
        communityMapper.insertPost(postDTO);

        if (imageUrls != null && !imageUrls.isEmpty()) {
            for (int i = 0; i < imageUrls.size(); i++) {
                PostImageDTO postImageDTO = PostImageDTO.builder()
                        .postImageUrl(imageUrls.get(i))
                        .postId(postDTO.getPostId())
                        .postImageOrder(i)
                        .build();
                communityMapper.insertPostImage(postImageDTO);
            }
        }

        if (productIdList != null && !productIdList.isEmpty()) {
            Map<String, Object> map = new HashMap<>();
            map.put("postId", postDTO.getPostId());
            map.put("productIds", new ArrayList<>(new HashSet<>(productIdList)));
            communityMapper.insertPostTags(map);
        }

        log.info("게시물 생성 완료, postId: {}", postDTO.getPostId());
        return postDTO.getPostId();
    }

    @Override
    public PostDTO getPostDetail(int postId) {
        PostDTO post = communityMapper.getPostDetail(postId);
        if (post == null) {
            log.warn("게시물 없음: postId={}", postId);
            return null;
        }
        return post;
    }

    @Override
    public List<ProductTag> getProductTagsByPostId(int postId) {
        List<ProductTag> productTags = communityMapper.getProductTagsByPostId(postId);
        return productTags != null ? productTags : Collections.emptyList();
    }

    @Override
    public List<PostDTO> getOtherStylesByUserId(int userId, int excludePostId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("excludePostId", excludePostId);
        List<PostDTO> otherStyles = communityMapper.getOtherStylesByUserId(params);
        return otherStyles != null ? otherStyles : Collections.emptyList();
    }

    @Override
    public List<PostImageDTO> getPostImagesByPostId(int postId) {
        List<PostImageDTO> postImages = communityMapper.getPostImagesByPostId(postId);
        return postImages != null ? postImages : Collections.emptyList();
    }

    @Override
    public List<PostDTO> getAllPosts() {
        return communityMapper.getAllPosts();
    }

    @Override
    public PostDTO getPostById(int id) {
        return communityMapper.getPostById(id);
    }

    private String uploadImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            log.warn("업로드된 파일이 없음.");
            return null;
        }

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path uploadPath = Paths.get(uploadDir, fileName);

        try {
            if (!Files.exists(uploadPath.getParent())) {
                Files.createDirectories(uploadPath.getParent());
                log.info("업로드 디렉토리 생성: {}", uploadPath.getParent());
            }
            Files.write(uploadPath, file.getBytes());
            log.info("이미지 업로드 성공: {}", uploadPath);
        } catch (IOException e) {
            log.error("이미지 업로드 실패: {}", fileName, e);
            throw e;
        }

        return "/uploads/" + fileName;
    }

    @Override
    @Transactional
    public void updatePost(PostDTO postDTO, List<Long> productIdList, List<MultipartFile> images) throws IOException {
        // 상품 태그 업데이트: 새 태그만 추가 (기존 태그 유지)
        if (productIdList != null && !productIdList.isEmpty()) {
            List<Long> newProductIds = new ArrayList<>(new HashSet<>(productIdList)); // 중복 제거
            List<ProductTag> existingTags = communityMapper.getProductTagsByPostId(postDTO.getPostId());
            List<Long> existingProductIds = existingTags.stream()
                    .map(ProductTag::getProductId)
                    .toList();

            // 추가할 태그: 새 리스트에 있지만 기존 리스트에 없는 경우
            List<Long> tagsToAdd = newProductIds.stream()
                    .filter(id -> !existingProductIds.contains(id))
                    .toList();

            if (!tagsToAdd.isEmpty()) {
                Map<String, Object> addMap = new HashMap<>();
                addMap.put("postId", postDTO.getPostId());
                addMap.put("productIds", tagsToAdd);
                communityMapper.insertPostTags(addMap);
            }
        }

        // 이미지 업데이트: 새 이미지만 추가 (기존 이미지 유지)
        if (images != null && !images.isEmpty()) {
            List<PostImageDTO> existingImages = communityMapper.getPostImagesByPostId(postDTO.getPostId());
            int nextImageOrder = existingImages.size(); // 기존 이미지 다음 순서부터 시작

            List<String> newImageUrls = new ArrayList<>();
            for (MultipartFile image : images) {
                String imageUrl = uploadImage(image);
                if (imageUrl != null) { // null 체크
                    newImageUrls.add(imageUrl);
                    PostImageDTO postImageDTO = PostImageDTO.builder()
                            .postImageUrl(imageUrl)
                            .postId(postDTO.getPostId())
                            .postImageOrder(nextImageOrder++)
                            .build();
                    communityMapper.insertPostImage(postImageDTO);
                }
            }

            // 썸네일이 없으면 첫 번째 새 이미지를 썸네일로 설정
            if (!newImageUrls.isEmpty() && (postDTO.getThumbnailImageUrl() == null || postDTO.getThumbnailImageUrl().isEmpty())) {
                postDTO.setThumbnailImageUrl(newImageUrls.get(0));
            }
        }

        // 모든 수정 작업 후 게시글 업데이트
        communityMapper.updatePost(postDTO, productIdList, images);
        log.info("게시물 수정 완료, postId: {}", postDTO.getPostId());
    }


}