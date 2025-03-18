package kr.co.fitzcode.user.service;

import kr.co.fitzcode.common.dto.UserDTO;
import kr.co.fitzcode.common.enums.UserRole;
import kr.co.fitzcode.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void registerUser(UserDTO dto) {
        // 비밀번호 암호화
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        userMapper.insertUser(dto);
        userMapper.insertUserTier(dto.getNickname());
    }

    @Override
    public boolean emailDuplicate(String email) {
        int count = userMapper.emailDuplicate(email);
        System.out.println("이메일 중복 체크 SQL 실행 결과: " + count);
        return count > 0;
    }

    @Override
    public boolean nicknameDuplicate(String nickname) {
        int count = userMapper.nicknameDuplicate(nickname);
        System.out.println("닉네임 중복 체크 SQL 실행 결과: " + count);
        return count > 0;
    }

    @Override
    public boolean phoneNumberDuplicate(String phoneNumber) {
        int count = userMapper.phoneNumberDuplicate(phoneNumber);
        System.out.println("전화번호 중복 체크 SQL 실행 결과: " + count);
        return count > 0;
    }

    @Override
    public void updatePw(UserDTO dto) {
        // 비밀번호 암호화
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        userMapper.updatePw(dto);
    }

    @Override
    public UserDTO findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    @Override
    public List<Integer> getUserRolesByUserId(int userId) {
        return userMapper.getUserRolesByUserId(userId);
    }

    @Override
    public List<UserRole> findRolesInStringByUserId(int userId) {
        List<Integer> roleIds = userMapper.getUserRolesByUserId(userId);
        return roleIds.stream()
                .map(roleId -> switch (roleId) {
                    case 1 -> UserRole.USER;
                    case 2 -> UserRole.ADMIN;
                    case 3 -> UserRole.LOGISTICS;
                    case 4 -> UserRole.INQUIRY;
                    default -> UserRole.USER;
                })
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO findByUserNaverId(String naverId) {
        return userMapper.findByUserNaverId(naverId);
    }

    @Override
    public UserDTO findBykakaoId(String kakaoId) {
        return userMapper.findByUserKakaoId(kakaoId);
    }

    @Override
    public void updateUserNaver(UserDTO user) {
        userMapper.updateUserNaver(user);
    }

    @Override
    public String findEmailByNameAndPhoneNumber(String userName, String phoneNumber) {
        return userMapper.findEmailByNameAndPhoneNumber(userName, phoneNumber);
    }
}