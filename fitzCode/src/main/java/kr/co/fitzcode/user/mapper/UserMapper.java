package kr.co.fitzcode.user.mapper;

import kr.co.fitzcode.common.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    void insertUser(UserDTO dto);
    void insertUserTier(@Param("nickname") String nickname);
    int emailDuplicate(@Param("email") String email);
    int nicknameDuplicate(@Param("nickname") String nickname);
    int phoneNumberDuplicate(@Param("phoneNumber") String phoneNumber);
    void updatePw(UserDTO dto);
    UserDTO findByEmail(String email);
    // CommonUserController 에서 사용
    UserDTO getUserByEmail(String email);
    List<Integer> getUserRolesByUserId(int userId);
    UserDTO findByUserNaverId(String naverId);
    void updateUserNaver(UserDTO user);
    UserDTO findByUserKakaoId(String kakaoId);
    String findEmailByNameAndPhoneNumber(@Param("userName") String userName, @Param("phoneNumber") String phoneNumber);

}