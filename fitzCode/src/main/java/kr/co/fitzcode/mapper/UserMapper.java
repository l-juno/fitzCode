package kr.co.fitzcode.mapper;

import kr.co.fitzcode.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {
    void insertUser(UserDTO dto);
    void insertUserTier(@Param("nickname") String nickname);
    int emailDuplicate(@Param("email") String email);
    int nicknameDuplicate(@Param("nickname") String nickname);
    int phoneNumberDuplicate(@Param("phoneNumber") String phoneNumber);
    void updatePw(UserDTO dto);
    UserDTO loginUser(String email, String password);
}
