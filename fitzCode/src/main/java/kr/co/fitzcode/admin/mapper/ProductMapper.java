package kr.co.fitzcode.admin.mapper;

import kr.co.fitzcode.admin.dto.ProductDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {

    void insertProduct(ProductDTO productDTO);

    List<ProductDTO> getAllProducts();

    ProductDTO getProductById(@Param("productId") Long productId);

    void updateProduct(ProductDTO productDTO);

    void deleteProduct(@Param("productId") Long productId);
}