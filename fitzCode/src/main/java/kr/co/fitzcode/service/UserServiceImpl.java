package kr.co.fitzcode.service;

import kr.co.fitzcode.mapper.UserMapper;
import kr.co.fitzcode.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public void insertUser(UserDTO dto) {
        userMapper.insertUser(dto);
    }

    @Override
    public boolean emailDuplicate(String email) {
        int count = userMapper.emailDuplicate(email);
        System.out.println("이메일 중복 체크 SQL 실행 결과: " + count);
        return count > 0;
    }

    @Override
    public boolean nickNameDuplicate(String nickName) {
        int count = userMapper.nickNameDuplicate(nickName);
        System.out.println("닉네임 중복 체크 SQL 실행 결과: " + count);
        return count > 0;
    }

    @Override
    public boolean phoneNumberDuplicate(String phoneNumber) {
        int count = userMapper.phoneNumberDuplicate(phoneNumber);
        System.out.println("전화번호 중복 체크 SQL 실행 결과: " + count);
        return count > 0;
    }

}
