package kr.co.fitzcode.admin.mapper;

import kr.co.fitzcode.admin.dto.MemberDTO;
import kr.co.fitzcode.admin.dto.MemberDetailDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MembersMapper {
    List<MemberDTO> getAllMembers(@Param("limit") int limit, @Param("offset") int offset);
    int getTotalMemberCount();
    MemberDetailDTO getMemberDetail(@Param("userId") int userId);
    void deleteMember(@Param("userId") int userId);
    void updateUserTier(@Param("userId") int userId, @Param("tierLevel") int tierLevel);
}