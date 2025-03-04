package kr.co.fitzcode.service;

import kr.co.fitzcode.mapper.UserMapper;
import kr.co.fitzcode.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userDao;

    @Override
    public void insertUser(UserDto dto) {
        userDao.insertUser(dto);
    }

    @Override
    public boolean emailDuplicate(String email) {
        int count = userDao.emailDuplicate(email);
        return count > 0;
    }

    @Override
    public boolean nickNameDuplicate(String nickname) {
        int count = userDao.nickNameDuplicate(nickname);
        return count > 0;
    }

    @Override
    public boolean phoneNumberDuplicate(String phonenumber) {
        int count = userDao.phoneNumberDuplicate(phonenumber);
        return count > 0;
    }

}
