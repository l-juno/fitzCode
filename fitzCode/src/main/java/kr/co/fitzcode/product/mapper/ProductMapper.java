package kr.co.fitzcode.product.mapper;

import kr.co.fitzcode.product.dto.ProductDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface ProductMapper {
    List<ProductDTO> getProductsByPage(HashMap<String, Object> params);

    List<ProductDTO> getAllProducts();

    ProductDTO getProductById(int productId);

    void insertProduct(ProductDTO productDTO);

    void insertManyProducts(List<ProductDTO> productDTOList);

    void updateProduct(ProductDTO productDTO);

    void deleteProductById(int productId);

}
