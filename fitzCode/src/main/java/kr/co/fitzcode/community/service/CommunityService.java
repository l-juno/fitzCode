package kr.co.fitzcode.community.service;

import kr.co.fitzcode.common.dto.ProductDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CommunityService {
    List<ProductDTO> searchProductsByName(String productName, int offset);
}
