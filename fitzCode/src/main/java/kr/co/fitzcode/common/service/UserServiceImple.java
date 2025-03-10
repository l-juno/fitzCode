package kr.co.fitzcode.common.service;

import kr.co.fitzcode.common.dto.UserDTO;
import kr.co.fitzcode.common.enums.UserRole;
import kr.co.fitzcode.common.mapper.UserMapper;
import kr.co.fitzcode.common.mapper.UserRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImple implements UserService {
    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;


    @Override
    public UserDTO getUserByNickname(String nickname) {
        return userMapper.getUserByNickname(nickname);
    }

    @Override
    public List<Integer> getUserRolesByUserId(int userId) {
        return userRoleMapper.findRolesByUserId(userId);
    }

    @Override
    public List<UserRole> findRolesInStringByUserId(int userId) {
        return userRoleMapper.findRolesInStringByUserId(userId);
    }

    @Override
    public Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            return userDetails.getUserId();
        }
        return null;
    }


}
