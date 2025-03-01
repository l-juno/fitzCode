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

    // 전체 회원 목록 조회 (페이지네이션)
//    @Override
//    public Map<String, Object> getAllMembers(int page, int size) {
//        int totalCount = membersMapper.getTotalMemberCount();
//        int offset = (page - 1) * size;
//        List<MemberDTO> members = membersMapper.getAllMembers(size, offset);
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("members", members);
//        response.put("totalCount", totalCount);
//        return response;
//    }

    // 회원 상세 정보 조회
    @Override
    public MemberDetailDTO getMemberDetail(int userId) {
        return membersMapper.getMemberDetail(userId);
    }

    // 회원 삭제
    @Override
    public void deleteMember(int userId) {
        membersMapper.deleteMember(userId);
    }

    // 회원 등급 업데이트
    @Override
    public void updateUserTier(int userId, int tierLevel) {
        membersMapper.updateUserTier(userId, tierLevel);
    }

    // 필터링된 회원 목록 조회 (역할 필터 및 정렬)
    @Override
    public Map<String, Object> getFilteredMembers(int page, int size, Integer roleId, String sortOrder) {
        int totalCount = membersMapper.getTotalFilteredMemberCount(roleId); // 필터링된 회원 총 수 조회
        int offset = (page - 1) * size; // 페이지네이션 offset 계산
        List<MemberDTO> members = membersMapper.getFilteredMemberList(size, offset, roleId, sortOrder); // 필터링된 회원 목록 조회

        Map<String, Object> response = new HashMap<>();
        response.put("members", members);
        response.put("totalCount", totalCount);
        response.put("currentPage", page);
        response.put("pageSize", size);
        return response;
    }
}