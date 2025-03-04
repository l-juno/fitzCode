package kr.co.fitzcode.user.service;

import kr.co.fitzcode.user.dto.UserDto;
import kr.co.fitzcode.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserMapper userMapper;

    @Override
    public UserDto getUserOne(int userId) {
        return userMapper.getUserOne(userId) ;
    }
}
