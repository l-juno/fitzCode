package kr.co.fitzcode.inquiry.service;

import kr.co.fitzcode.common.dto.InquiryDTO;
import kr.co.fitzcode.common.dto.UserDTO;
import kr.co.fitzcode.common.enums.InquiryCategory;
import kr.co.fitzcode.common.enums.InquiryStatus;
import kr.co.fitzcode.inquiry.mapper.InquiryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {
    private final InquiryMapper inquiryMapper;

    // 사용자 조회
    @Override
    public UserDTO getUserOne(int userId) {
        return inquiryMapper.getUserOne(userId) ;
    }

    // 문의 내용 등록
    @Override
    public void registInquiry(InquiryDTO inquiryDTO) {
        log.info(inquiryDTO.toString());

        // 공통 문의 내용
        InquiryDTO dto = new InquiryDTO();
        dto.setUserId(inquiryDTO.getUserId());
        dto.setOrderId(inquiryDTO.getOrderId());
        dto.setSubject(inquiryDTO.getSubject());
        dto.setContent(inquiryDTO.getContent());
        dto.setCategoryCode(inquiryDTO.getCategoryCode());
        dto.setStatusCode(InquiryStatus.PENDING.getCode());
        dto.setProductId(inquiryDTO.getProductId());
        inquiryMapper.saveInquiry(dto);
        log.info(">>>>>>>>>>>>>>> inquiry_id 확인 {}", dto.getInquiryId());
//        saveImages(dto.getInquiryId(), inquiryDTO.getImageUrls()); // 이미지 dto 에 담기
    }

    // 개인별 문의 내역 불러오기
    @Override
    public List<HashMap<String, Object>> getInquiryList(int userId) {
        List<HashMap<String, Object>> list = new ArrayList<>();
        List<InquiryDTO> inquiryDTOList = inquiryMapper.getInquiryList(userId);
        for (InquiryDTO inquiryDTO : inquiryDTOList) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("subject", inquiryDTO.getSubject());
            map.put("content", inquiryDTO.getContent());
            map.put("category", inquiryDTO.getCategoryCode());
            map.put("status", inquiryDTO.getStatus().getDescription());
            map.put("createdAt", inquiryDTO.getCreatedAt());
            map.put("inquiryId", inquiryDTO.getInquiryId());
            list.add(map);
        }
        return list;
    }

    // 문의 상세보기
    @Override
    public HashMap<String, Object> getInquiryDetail(int inquiryId) {
        // 상세보기 조회를 위한 데이터 가져오기
        HashMap<String, Object> data = getDataForDetail(inquiryId);
        // 상세보기 조회
        List<HashMap<String, Object>> detail = inquiryMapper.getInquiryDetail(data);
        // 상세보기 controller에 전달할 객체 설정
        HashMap<String, Object> detailList = getInquiryDetailList(detail);
        log.info(">>>>>>>>>> finalDetail {}", detailList.toString());
        return detailList;
    }

    // 상품 찾기
    @Override
    public List<HashMap<String, Object>> searchProduct(String userInputProductName) {
        log.info(">>>>>>>>> 상품 검색 : {}", inquiryMapper.searchProduct(userInputProductName));
        return inquiryMapper.searchProduct(userInputProductName);
    }

    @Override
    public HashMap<String, Object> selectedProduct(int productId) {
        log.info(">>>>>>>>> 선택된 상품 : {} ", inquiryMapper.selectedProduct(productId));
        return inquiryMapper.selectedProduct(productId);
    }

    // 주문 내역 불러오기
    @Override
    public List<HashMap<String, Object>> getUserAndOrderList(int userId) {
        return inquiryMapper.getUserAndOrderList(userId);
    }

//    // 이미지 저장하기
//    public void saveImages(int inquiryId, List<String> file) {
//        int index = 0;
//        for (String image : file) {
//            if (!image.isEmpty()) { // 파일이 비어있지 않다면
//                log.info(">>>>>>>>>>> : 파일 비어있는지 {} ", image.isEmpty());
//                log.info(">>>>>>>>>>> : 파일 크기 {} ", image.getSize());
//                log.info(">>>>>>>>>>> : 파일 이름 {} ", image.getOriginalFilename());
//                InquiryImageDTO imageDTO = InquiryImageDTO.builder()
//                        .inquiryId(inquiryId)
//                        .imageUrl(image.getOriginalFilename())
//                        .imageOrder(index)
//                        .build();
//                inquiryMapper.saveImages(imageDTO);
//                index++;
//            }
//        }
//    }

    // 문의 유형 int -> enum 바꾸기
    public InquiryCategory setCategory(int categoryCode) {
        return InquiryCategory.fromCode(categoryCode);
    }

    // 문의 상태 int -> enum 바꾸기
    public InquiryStatus setStatus(int status) {
        return InquiryStatus.fromCode(status);
    }

    // 상세보기위한 데이터 가져오기
    public HashMap<String, Object> getDataForDetail(int inquiryId) {
        // 상세보기를 위한 데이터 조회
        List<HashMap<String, Object>> dataList = inquiryMapper.getDataForDetail(inquiryId);

        // Map 에 담기
        HashMap<String, Object> firstData = dataList.get(0);
        HashMap<String, Object> dataMap = new HashMap<>();
        int category = (int) firstData.get("categoryCode"); // category 형변환

        dataMap.put("inquiryId", inquiryId);
        dataMap.put("category", category);

        dataMap.put("productId", firstData.get("productId"));
        dataMap.put("orderId", firstData.get("orderId"));

        List<Integer> imageIds = new ArrayList<>();
        for (HashMap<String, Object> data : dataList) {
            if (data.get("image_id") != null) {
                imageIds.add((Integer) data.get("image_id"));
            }
        }
        dataMap.put("imageIds", imageIds);
        return dataMap;
    }

    // 상세보기 controller 에 보낼 데이터
    public HashMap<String, Object> getInquiryDetailList(List<HashMap<String, Object>> detail) {
        HashMap<String, Object> firstData = detail.get(0);
        // detailList 에 담을 hashmap
        HashMap<String, Object> detailMap = new HashMap<>();
        detailMap.put("subject", firstData.get("subject"));
        detailMap.put("content", firstData.get("content"));
        detailMap.put("category", setCategory((Integer) firstData.get("categoryCode")).getDescription());
        detailMap.put("status", setStatus((Integer) firstData.get("statusCode")).getDescription());
        detailMap.put("createdAt", firstData.get("created_at"));
        detailMap.put("userName", firstData.get("user_name"));
        detailMap.put("phoneNumber", firstData.get("phone_number"));
        detailMap.put("reply", firstData.get("reply")); // 답변
        detailMap.put("productImageUrl", firstData.get("productImageUrl")); // 상품 이미지
        detailMap.put("productName", firstData.get("product_name")); // 상품명
        detailMap.put("orderId", firstData.get("order_id")); // 주문 id
        detailMap.put("orderPrice", firstData.get("order_price")); // 주문 가격
        detailMap.put("productBrand", firstData.get("brand")); // 상품 브랜드
        List<String> imageUrlList = new ArrayList<>();
        for (HashMap<String, Object> imageUrls : detail) {
            if (imageUrls.get("inquiryImageUrl") != null) {
                imageUrlList.add((String) imageUrls.get("inquiryImageUrl"));
            }
            detailMap.put("imageUrlList", imageUrlList);
        }
        return detailMap;
    }

}