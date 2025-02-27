package kr.co.fitzcode.admin.service;

import kr.co.fitzcode.admin.dto.MemberDTO;
import kr.co.fitzcode.admin.dto.MemberDetailDTO;
import kr.co.fitzcode.admin.mapper.MembersMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MembersServiceImpl implements MembersService {
    private final MembersMapper membersMapper;

    /**
     * 회원 목록 조회 (페이징 처리)
     * @param page 조회할 페이지 번호
     * @param size 페이지당 조회할 회원 수
     * @return 회원 목록과 전체 회원 수를 포함한 Map 객체
     */
    @Override
    public Map<String, Object> getAllMembers(int page, int size) {
        int totalCount = membersMapper.getTotalMemberCount(); // 전체 회원 수 조회
        int offset = (page - 1) * size; // 페이지네이션을 위한 offset 계산
        List<MemberDTO> members = membersMapper.getAllMembers(size, offset); // 회원 목록 조회

        Map<String, Object> response = new HashMap<>();
        response.put("members", members); // 회원 목록 추가
        response.put("totalCount", totalCount); // 전체 회원 수 추가
        return response;
    }

    /**
     * 특정 회원 상세 조회
     * @param userId 조회할 회원 ID
     * @return 회원 상세 정보 DTO
     */
    @Override
    public MemberDetailDTO getMemberDetail(int userId) {
        return membersMapper.getMemberDetail(userId);
    }

    /**
     * 특정 회원 삭제
     * @param userId 삭제할 회원 ID
     */
    @Override
    public void deleteMember(int userId) {
        membersMapper.deleteMember(userId);
    }

    /**
     * 회원 등급 변경
     * @param userId 회원 ID
     * @param tierLevel 변경할 회원 등급
     */
    @Override
    public void updateUserTier(int userId, int tierLevel) {
        membersMapper.updateUserTier(userId, tierLevel);
    }
}