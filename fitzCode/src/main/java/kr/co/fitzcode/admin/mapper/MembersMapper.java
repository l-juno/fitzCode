package kr.co.fitzcode.admin.mapper;

import kr.co.fitzcode.admin.dto.MemberDTO;
import kr.co.fitzcode.admin.dto.MemberDetailDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MembersMapper {

    // 전체 회원 목록 조회 (페이징 처리)
    List<MemberDTO> getAllMembers(@Param("limit") int limit, @Param("offset") int offset);

    // 전체 회원 수 조회 (페이징)
    int getTotalMemberCount();

    // 특정 회원의 상세 정보 조회
    MemberDetailDTO getMemberDetail(@Param("userId") int userId);

    // 특정 회원 삭제
    void deleteMember(@Param("userId") int userId);

    // 회원 등급 업데이트
    void updateUserTier(@Param("userId") int userId, @Param("tierLevel") int tierLevel);

    // 필터링된 회원의 총 개수를 조회 (roleId 기준)
    int getTotalFilteredMemberCount(@Param("roleId") Integer roleId);

    // 필터링된 회원 목록 조회 (roleId 기준)
    List<MemberDTO> getFilteredMemberList(@Param("limit") int limit,
                                          @Param("offset") int offset,
                                          @Param("roleId") Integer roleId,
                                          @Param("sortOrder") String sortOrder);
}