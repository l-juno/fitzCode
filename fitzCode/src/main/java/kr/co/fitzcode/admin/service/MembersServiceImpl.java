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
        int totalCount = membersMapper.getTotalMemberCount();
        int offset = (page - 1) * size;
        List<MemberDTO> members = membersMapper.getAllMembers(size, offset);

        Map<String, Object> response = new HashMap<>();
        response.put("members", members);
        response.put("totalCount", totalCount);
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