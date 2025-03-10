package kr.co.fitzcode.user.service;


import kr.co.fitzcode.common.dto.UserDTO;

public interface UserService {
    void registerUser(UserDTO dto);
//    void insertUser(UserDTO dto);
    boolean emailDuplicate(String email);
    boolean nicknameDuplicate(String nickname);
    boolean phoneNumberDuplicate(String phoneNumber);
    void updatePw(UserDTO dto);
    UserDTO loginUser(String email, String password);

}


