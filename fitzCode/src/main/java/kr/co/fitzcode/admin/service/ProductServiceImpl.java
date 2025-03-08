package kr.co.fitzcode.admin.service;

import kr.co.fitzcode.admin.dto.ProductCategoryDTO;
import kr.co.fitzcode.admin.dto.ProductDTO;
import kr.co.fitzcode.admin.dto.ProductSizeDTO;
import kr.co.fitzcode.admin.mapper.ProductMapper;
import kr.co.fitzcode.common.enums.ProductSize;
import kr.co.fitzcode.common.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final S3Service s3Service;

    // 상품 추가
    @Override
    @Transactional
    public void addProduct(ProductDTO productDTO, MultipartFile mainImageFile, List<MultipartFile> additionalImageFiles) {
        // 메인 이미지 업로드
        if (mainImageFile != null && !mainImageFile.isEmpty()) {
            String mainImageUrl = s3Service.uploadFile(mainImageFile, "products-images/main");
            productDTO.setImageUrl(mainImageUrl);
        }

        // 추가 이미지 업로드
        if (additionalImageFiles != null && !additionalImageFiles.isEmpty()) {
            List<String> additionalImageUrls = s3Service.uploadFiles(additionalImageFiles, "products-images/additional");
            productDTO.setAdditionalImages(additionalImageUrls);
        }

        // 상품 기본 정보 삽입
        productMapper.insertProduct(productDTO);
        Long productId = productDTO.getProductId(); // 삽입 후 생성된 ID 가져오기

        // 추가 이미지 삽입
        if (productDTO.getAdditionalImages() != null && !productDTO.getAdditionalImages().isEmpty()) {
            int imageOrder = 1;
            for (String imageUrl : productDTO.getAdditionalImages()) {
                productMapper.insertProductImage(productId, imageUrl, imageOrder++);
            }
        }

        // 사이즈별 재고 넣기
        if (productDTO.getProductSizes() != null && !productDTO.getProductSizes().isEmpty()) {
            for (ProductSizeDTO size : productDTO.getProductSizes()) {
                productMapper.insertProductSize(productId, size.getSizeCode(), size.getStock());
            }
        }
    }

    // 전체 상품 조회 (페이지네이션, 정렬, 검색 적용)
    @Override
    public List<ProductDTO> getAllProducts(int page, int pageSize, String sort, String keyword) {
        int offset = (page - 1) * pageSize;
        return productMapper.getAllProducts(offset, pageSize, sort, keyword);
    }

    // 특정 카테고리 상품 조회 (페이지네이션, 정렬, 검색 적용)
    @Override
    public List<ProductDTO> getProductsByCategory(Long categoryId, int page, int pageSize, String sort, String keyword) {
        int offset = (page - 1) * pageSize;
        return productMapper.getProductsByCategory(categoryId, offset, pageSize, sort, keyword);
    }

    // 특정 상위 카테고리의 모든 하위 카테고리 상품 조회 (페이지네이션, 정렬, 검색 적용)
    @Override
    public List<ProductDTO> getProductsByParentCategory(Long parentCategoryId, int page, int pageSize, String sort, String keyword) {
        int offset = (page - 1) * pageSize;
        return productMapper.getProductsByParentCategory(parentCategoryId, offset, pageSize, sort, keyword);
    }

    // 전체 상품 개수 조회 (검색 적용)
    @Override
    public int countAllProducts(String keyword) {
        return productMapper.countAllProducts(keyword);
    }

    // 특정 카테고리 상품 개수 조회 (검색 적용)
    @Override
    public int countProductsByCategory(Long categoryId, String keyword) {
        return productMapper.countProductsByCategory(categoryId, keyword);
    }

    // 특정 상위 카테고리 상품 개수 조회 (검색 적용)
    @Override
    public int countProductsByParentCategory(Long parentCategoryId, String keyword) {
        return productMapper.countProductsByParentCategory(parentCategoryId, keyword);
    }

    // 상위 카테고리 목록 조회
    @Override
    public List<ProductCategoryDTO> getParentCategories() {
        return productMapper.getParentCategories();
    }

    // 특정 상위 카테고리의 하위 카테고리 목록 조회
    @Override
    public List<ProductCategoryDTO> getChildCategories(Long parentId) {
        return productMapper.getChildCategories(parentId);
    }

    // 상위 카테고리 -> 사이즈 조회
    @Override
    public List<Map<String, Object>> getSizesByParentCategory(Long parentId) {
        if (parentId == 1) { // 신발
            return Arrays.stream(ProductSize.values())
                    .filter(size -> size.getCode() <= 9)
                    .map(size -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("code", size.getCode());
                        map.put("description", size.getDescription());
                        return map;
                    })
                    .collect(Collectors.toList());
        } else if (parentId == 2 || parentId == 3) { // 상의 또는 하의
            return Arrays.stream(ProductSize.values())
                    .filter(size -> size.getCode() >= 10)
                    .map(size -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("code", size.getCode());
                        map.put("description", size.getDescription());
                        return map;
                    })
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}