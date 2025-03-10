package kr.co.fitzcode.inquiry.service;

import kr.co.fitzcode.common.dto.InquiryDTO;
import kr.co.fitzcode.common.dto.UserDTO;

import java.util.HashMap;
import java.util.List;

public interface InquiryService {
    UserDTO getUserOne(int userId);

//    void registInquiry(InquiryDTO inquiryDTO);

    List<HashMap<String, Object>> getUserAndOrderList(int userId);

    List<HashMap<String, Object>> getInquiryList(int userId);

    HashMap<String, Object> getInquiryDetail(int inquiryId);

    List<HashMap<String, Object>> searchProduct(String userInputProductName);

    HashMap<String, Object> selectedProduct(int productId);
}
