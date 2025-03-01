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

    @Override
    public MemberDetailDTO getMemberDetail(int userId) {
        return membersMapper.getMemberDetail(userId);
    }

    @Override
    public void deleteMember(int userId) {
        membersMapper.deleteMember(userId);
    }

    @Override
    public void updateUserTier(int userId, int tierLevel) {
        membersMapper.updateUserTier(userId, tierLevel);
    }
}