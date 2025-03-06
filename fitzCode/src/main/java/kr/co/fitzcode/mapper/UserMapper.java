package kr.co.fitzcode.mapper;

import kr.co.fitzcode.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {
    void insertUser(UserDTO dto);
    int emailDuplicate(@Param("email") String email);
    int nickNameDuplicate(@Param("nickName") String nickName);
    int phoneNumberDuplicate(@Param("phoneNumber") String phoneNumber);
}
