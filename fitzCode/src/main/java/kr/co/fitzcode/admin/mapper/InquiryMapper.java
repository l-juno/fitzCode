package kr.co.fitzcode.admin.mapper;

import kr.co.fitzcode.admin.dto.InquiryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InquiryMapper {

    // 답변이 없는 문의의 개수를 조회
    int getUnansweredInquiryCount();

    // 전체 문의 목록을 조회 (페이징 처리)
    List<InquiryDTO> getInquiryList(@Param("limit") int limit, @Param("offset") int offset);

    // 특정 문의의 상세 정보를 조회
    InquiryDTO getInquiryDetail(@Param("inquiryId") int inquiryId);

    // 특정 문의의 상태를 업데이트
    void updateInquiryStatus(@Param("inquiryId") int inquiryId, @Param("status") int status);

    // 특정 문의의 카테고리를 업데이트
    void updateInquiryCategory(@Param("inquiryId") int inquiryId, @Param("category") int category);

    // 특정 문의에 답변과 상태를 업데이트
    void updateInquiryReplyAndStatus(@Param("inquiryId") int inquiryId,
                                     @Param("reply") String reply,
                                     @Param("status") int status);

    // 문의 등록
    void insertInquiry(InquiryDTO inquiryDTO);

    // 이미지 삽입
    void insertInquiryImage(@Param("inquiryId") int inquiryId,
                            @Param("imageUrl") String imageUrl,
                            @Param("imageOrder") int imageOrder);

    // 문의에 첨부된 이미지 개수 조회
    int countImagesByInquiryId(@Param("inquiryId") int inquiryId);

    // 전체 문의 수를 조회 (페이지네이션 용)
    int getTotalInquiryCount();

    // 필터링된 문의의 총 개수를 조회 (카테고리와 상태 코드 기준)
    int getTotalFilteredInquiryCount(@Param("categoryIds") List<Integer> categoryIds,
                                     @Param("statusCodes") List<Integer> statusCodes);

    // 필터링된 문의 목록을 조회 (카테고리와 상태 코드 기준)
    List<InquiryDTO> getFilteredInquiryList(@Param("limit") int limit,
                                            @Param("offset") int offset,
                                            @Param("categoryIds") List<Integer> categoryIds,
                                            @Param("statusCodes") List<Integer> statusCodes);
}