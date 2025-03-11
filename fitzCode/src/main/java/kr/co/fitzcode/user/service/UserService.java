package kr.co.fitzcode.user.service;

import kr.co.fitzcode.common.dto.UserDTO;
import kr.co.fitzcode.common.enums.UserRole;

import java.util.List;

public interface UserService {
    void registerUser(UserDTO dto);
    boolean emailDuplicate(String email);
    boolean nicknameDuplicate(String nickname);
    boolean phoneNumberDuplicate(String phoneNumber);
    void updatePw(UserDTO dto);
    UserDTO findByEmail(String email);
    UserDTO getUserByEmail(String email);
    List<Integer> getUserRolesByUserId(int userId);
    List<UserRole> findRolesInStringByUserId(int userId);
    UserDTO findByUserNaverId(String naverId);
    void updateUser(UserDTO user);

}