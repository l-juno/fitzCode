package kr.co.fitzcode.community.mapper;

import kr.co.fitzcode.common.dto.ProductDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommunityMapper {
    List<ProductDTO> searchProductsByName(String productName, int offset);
}
