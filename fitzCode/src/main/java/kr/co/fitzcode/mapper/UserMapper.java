package kr.co.fitzcode.mapper;

import kr.co.fitzcode.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {
    void insertUser(UserDto dto);
    int emailDuplicate(String email);
    int nickNameDuplicate(String nickname);
    int phoneNumberDuplicate(String phonenumber);
}
