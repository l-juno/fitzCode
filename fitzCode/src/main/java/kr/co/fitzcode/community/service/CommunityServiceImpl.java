package kr.co.fitzcode.community.service;

import kr.co.fitzcode.common.dto.ProductDTO;
import kr.co.fitzcode.community.mapper.CommunityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommunityServiceImpl implements CommunityService {

    private final CommunityMapper communityMapper;

    // 상품 이름으로 검색하여 상품 리스트 반환
    public List<ProductDTO> searchProductsByName(String productName, int offset) {
        return communityMapper.searchProductsByName(productName, offset);
    }
}
