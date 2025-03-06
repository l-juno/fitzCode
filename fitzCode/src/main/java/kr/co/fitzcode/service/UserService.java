package kr.co.fitzcode.service;


import kr.co.fitzcode.dto.UserDTO;

public interface UserService {
    void insertUser(UserDTO dto);
    boolean emailDuplicate(String email);
    boolean nickNameDuplicate(String nickName);
    boolean phoneNumberDuplicate(String phoneNumber);
}
