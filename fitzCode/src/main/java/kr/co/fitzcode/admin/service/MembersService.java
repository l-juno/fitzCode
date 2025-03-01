package kr.co.fitzcode.admin.service;

import kr.co.fitzcode.admin.dto.MemberDetailDTO;

import java.util.Map;

public interface MembersService {

    Map<String, Object> getAllMembers(int page, int size);

    MemberDetailDTO getMemberDetail(int userId);

    void deleteMember(int userId);

    void updateUserTier(int userId, int tierLevel);
}