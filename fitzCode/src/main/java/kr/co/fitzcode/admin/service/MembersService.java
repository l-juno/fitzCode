package kr.co.fitzcode.admin.service;

import kr.co.fitzcode.admin.dto.MemberDTO;
import kr.co.fitzcode.admin.dto.MemberDetailDTO;

import java.util.Map;

public interface MembersService {

    // 모든 회원 목록 조회 (페이지네이션 포함)
//    Map<String, Object> getAllMembers(int page, int size);

    // 회원의 상세 정보 조회
    MemberDetailDTO getMemberDetail(int userId);

    // 회원 삭제
    void deleteMember(int userId);

    // 회원 등급 업데이트
    void updateUserTier(int userId, int tierLevel);

    // 필터링된 회원 목록 조회 (역할 필터, 정렬 기준)
    Map<String, Object> getFilteredMembers(int page, int size, Integer roleId, String sortOrder);
}