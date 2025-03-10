package kr.co.fitzcode.inquiry.mapper;

import kr.co.fitzcode.inquiry.dto.InquiryDTO;
import kr.co.fitzcode.inquiry.dto.InquiryImageDTO;
import kr.co.fitzcode.inquiry.dto.ProductDTO;
import kr.co.fitzcode.inquiry.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface InquiryMapper {
    UserDTO getUserOne(int userId);
    public void saveImages(@Param("imageDTO") InquiryImageDTO imageDTO);
    void saveInquiry(@Param("inquiryDTO") InquiryDTO inquiryDTO);
    List<HashMap<String, Object>> getUserAndOrderList(@Param("userId") int userId);
    List<InquiryDTO> getInquiryList(@Param("userId") int userId);
    List<HashMap<String, Object>> getDataForDetail(@Param("inquiryId") int inquiryId);
    List<HashMap<String, Object>> getInquiryDetail(@Param("inquiryMap") HashMap<String, Object> data);
    List<HashMap<String, Object>> searchProduct(@Param("userInput") String userInputProductName);
    HashMap<String, Object> selectedProduct(@Param("productId") int productId);
}
