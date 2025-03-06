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

        List<ProductImageDTO> images = productDetailMapper.findProductImagesById(productId);
        System.out.println("Images retrieved: " + (images != null ? images.size() : "null"));
        if (images != null) {
            for (ProductImageDTO img : images) {
                if (img.getImageUrl() == null || img.getImageUrl().isEmpty()) {
                    img.setImageUrl("/images/fallback.jpg");
                }
            }
        }
        product.setImages(images != null ? images : new ArrayList<>());

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
        if (discountedPrice == null) {
            discountedPrice = 0;
        }
        productDetailMapper.updateDiscountedPrice(productId, discountedPrice);
    }

    @Override
    public List<ProductSizeDTO> getSizesByProductId(Long productId) {
        return productDetailMapper.findSizesByProductId(productId);
    }

    @Override
    public void updateSizes(Long productId, List<ProductSizeDTO> sizes) {
        if (sizes != null) {
            for (ProductSizeDTO dto : sizes) {
                dto.setProductId(productId);
                System.out.println("Processing size: productSizeId=" + dto.getProductSizeId() + ", sizeCode=" + dto.getSizeCode() + ", stock=" + dto.getStock()); // 디버깅 로그
                if (dto.getSizeCode() == null) {
                    System.err.println("sizeCode is null for productId=" + productId + ", dto=" + dto);
                    throw new IllegalArgumentException("sizeCode cannot be null");
                }
                if (dto.getProductSizeId() == null) {
                    productDetailMapper.insertProductSize(dto);
                } else {
                    productDetailMapper.updateProductSizeStock(dto);
                }
            }
        }
    }
}