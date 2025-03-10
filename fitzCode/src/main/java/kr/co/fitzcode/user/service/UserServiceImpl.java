package kr.co.fitzcode.user.service;

import kr.co.fitzcode.common.dto.UserDTO;
import kr.co.fitzcode.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public void registerUser(UserDTO dto) {
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
        userMapper.updatePw(dto);
    }

    @Override
    public UserDTO loginUser(String email, String password) {
        return userMapper.loginUser(email, password);
    }


}
