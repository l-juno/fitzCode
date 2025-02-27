package kr.co.fitzcode.admin.service;

import kr.co.fitzcode.admin.dto.MemberDetailDTO;

import java.util.Map;

public interface MembersService {

    /**
     * 회원 목록 조회 (페이징 처리)
     * @param page 조회할 페이지 번호
     * @param size 페이지당 조회할 회원 수
     * @return 회원 목록 및 총 회원 수를 포함한 Map 객체
     */
    Map<String, Object> getAllMembers(int page, int size);

    /**
     * 특정 회원 상세 조회
     * @param userId 조회할 회원 ID
     * @return 회원 상세 정보 DTO
     */
    MemberDetailDTO getMemberDetail(int userId);

    /**
     * 특정 회원 삭제
     * @param userId 삭제할 회원 ID
     */
    void deleteMember(int userId);

    /**
     * 회원 등급 변경
     * @param userId 회원 ID
     * @param tierLevel 변경할 회원 등급
     */
    void updateUserTier(int userId, int tierLevel);
}