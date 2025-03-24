package kr.co.fitzcode.community.service;

import kr.co.fitzcode.common.dto.PostDTO;
import kr.co.fitzcode.common.dto.PostImageDTO;
import kr.co.fitzcode.common.dto.PostLikeDTO;
import kr.co.fitzcode.common.dto.ProductTag;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface CommunityService {
    List<ProductTag> searchProductsByName(String productName);
    int insertPost(PostDTO postDTO, List<Long> productIdList, List<MultipartFile> images) throws IOException;
    Map<String, Object> getPostDetail(int postId);
    List<ProductTag> getProductTagsByPostId(int postId);
    List<Map<String, Object>> getOtherStylesByUserId(int userId, int excludePostId); // 파라미터 이름 변경
    List<PostImageDTO> getPostImagesByPostId(int postId);
    List<Map<String, Object>> getAllPosts(String styleCategory);
    PostDTO getPostById(int id);
    void updatePost(PostDTO postDTO, List<Long> productIdList, List<MultipartFile> images) throws IOException;
    List<PostDTO> findByStyleCategory(String styleCategory);
    void deletePost(int postId);

    boolean insertPostLike(PostLikeDTO postLikeDTO); // 좋아요 추가
    boolean deletePostLike(PostLikeDTO postLikeDTO); // 좋아요 삭제
    int countPostLikes(int postId);              // 좋아요 수 조회
    boolean isLiked(int postId, int userId); // 좋아요 상태 확인
}