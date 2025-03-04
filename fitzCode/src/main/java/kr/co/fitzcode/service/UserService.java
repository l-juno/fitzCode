package kr.co.fitzcode.service;


import kr.co.fitzcode.dto.UserDto;

public interface UserService {
    void insertUser(UserDto dto);
    boolean emailDuplicate(String email);
    boolean nickNameDuplicate(String nickname);
    boolean phoneNumberDuplicate(String phonenumber);
}
