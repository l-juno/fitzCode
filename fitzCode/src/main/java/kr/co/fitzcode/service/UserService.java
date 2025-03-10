package kr.co.fitzcode.service;


import kr.co.fitzcode.dto.UserDTO;

public interface UserService {
    void registerUser(UserDTO dto);
//    void insertUser(UserDTO dto);
    boolean emailDuplicate(String email);
    boolean nicknameDuplicate(String nickname);
    boolean phoneNumberDuplicate(String phoneNumber);
    void updatePw(UserDTO dto);
    UserDTO loginUser(String email, String password);

}


