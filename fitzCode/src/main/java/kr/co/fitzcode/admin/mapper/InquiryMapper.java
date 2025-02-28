package kr.co.fitzcode.admin.mapper;

import kr.co.fitzcode.admin.dto.InquiryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InquiryMapper {

    int getUnansweredInquiryCount();

    List<InquiryDTO> getInquiryList(@Param("limit") int limit, @Param("offset") int offset);

    InquiryDTO getInquiryDetail(@Param("inquiryId") int inquiryId);

    void updateInquiryStatus(@Param("inquiryId") int inquiryId, @Param("status") int status);

    void updateInquiryCategory(@Param("inquiryId") int inquiryId, @Param("category") int category);

    void updateInquiryReplyAndStatus(@Param("inquiryId") int inquiryId, @Param("reply") String reply, @Param("status") int status);

    int getTotalInquiryCount();

    // 필터링된 총 개수 조회
    int getTotalFilteredInquiryCount(@Param("categoryIds") List<Integer> categoryIds, @Param("statusCodes") List<Integer> statusCodes);

    // 필터링된 목록 조회
    List<InquiryDTO> getFilteredInquiryList(@Param("limit") int limit,
                                            @Param("offset") int offset,
                                            @Param("categoryIds") List<Integer> categoryIds,
                                            @Param("statusCodes") List<Integer> statusCodes);
}