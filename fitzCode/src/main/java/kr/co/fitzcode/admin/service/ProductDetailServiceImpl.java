package kr.co.fitzcode.admin.service;

import kr.co.fitzcode.admin.dto.ProductDetailDTO;
import kr.co.fitzcode.admin.dto.ProductImageDTO;
import kr.co.fitzcode.admin.dto.ProductSizeDTO;
import kr.co.fitzcode.admin.mapper.ProductDetailMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductDetailServiceImpl implements ProductDetailService {

    private final ProductDetailMapper productDetailMapper;

    @Override
    public ProductDetailDTO getProductDetail(Long productId) {
        ProductDetailDTO product = productDetailMapper.findProductDetailById(productId);
        if (product == null) {
            return null;
        }

        product.setImages(productDetailMapper.findProductImagesById(productId));

        List<ProductSizeDTO> sizes = productDetailMapper.findSizesByProductId(productId);
        if (sizes != null) {
            for (ProductSizeDTO size : sizes) {
                size.getSizeDescription();
            }
        }
        product.setSizes(sizes != null ? sizes : new ArrayList<>());

        return product;
    }

    @Override
    public void updateDiscountedPrice(Long productId, Integer discountedPrice) {
        // 할인 가격이 0일 경우에도 업데이트 d
        if (discountedPrice == null) {
            discountedPrice = 0;
        }
        productDetailMapper.updateDiscountedPrice(productId, discountedPrice);
    }

    @Override
    public List<ProductSizeDTO> getSizesByProductId(Long productId) {
        // 상품 사이즈 목록 조회
        return productDetailMapper.findSizesByProductId(productId);
    }

    @Override
    public void updateSizes(Long productId, List<ProductSizeDTO> sizes) {
        if (sizes != null) {
            for (ProductSizeDTO dto : sizes) {
                // productSizeId null -> 업데이트 x
                if (dto.getProductSizeId() != null) {
                    productDetailMapper.updateProductSizeStock(dto);
                }
            }
        }
    }
}