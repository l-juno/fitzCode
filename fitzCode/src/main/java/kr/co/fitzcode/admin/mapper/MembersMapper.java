package kr.co.fitzcode.admin.mapper;

import kr.co.fitzcode.admin.dto.MemberDTO;
import kr.co.fitzcode.admin.dto.MemberDetailDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MembersMapper {

    /**
     * 회원 목록 조회 (페이징 처리)
     * @param limit 페이지당 회원 수
     * @param offset 조회 시작 위치 (limit * (page - 1))
     * @return 회원 목록 리스트
     */
    List<MemberDTO> getAllMembers(@Param("limit") int limit, @Param("offset") int offset);

    /**
     * 전체 회원 수 조회
     * @return 총 회원 수
     */
    int getTotalMemberCount();

    /**
     * 특정 회원 상세 조회
     * @param userId 조회할 회원 ID
     * @return 회원 상세 정보 DTO
     */
    MemberDetailDTO getMemberDetail(@Param("userId") int userId);

    /**
     * 특정 회원 삭제
     * @param userId 삭제할 회원 ID
     */
    void deleteMember(@Param("userId") int userId);

    /**
     * 회원 등급 수정
     * @param userId 회원 ID
     * @param tierLevel 변경할 회원 등급
     */
    void updateUserTier(@Param("userId") int userId, @Param("tierLevel") int tierLevel);
}