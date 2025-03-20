package kr.co.fitzcode.community.service;

import kr.co.fitzcode.common.dto.PostDTO;
import kr.co.fitzcode.common.dto.PostImageDTO;
import kr.co.fitzcode.common.dto.ProductTag;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CommunityService {
    List<ProductTag> searchProductsByName(String productName);
    int insertPost(PostDTO postDTO, List<Long> productIdList, List<MultipartFile> images) throws IOException;
    PostDTO getPostDetail(int postId);
    List<ProductTag> getProductTagsByPostId(int postId);
    List<PostDTO> getOtherStylesByUserId(int userId, int excludePostId); // 파라미터 이름 변경
    List<PostImageDTO> getPostImagesByPostId(int postId);
    List<PostDTO> getAllPosts();
    PostDTO getPostById(int id);
    void updatePost(PostDTO postDTO, List<Long> productIdList, List<MultipartFile> images) throws IOException;
}