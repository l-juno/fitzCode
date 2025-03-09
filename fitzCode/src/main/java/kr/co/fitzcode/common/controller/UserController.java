package kr.co.fitzcode.common.controller;

import kr.co.fitzcode.common.dto.UserDTO;
import kr.co.fitzcode.common.enums.UserRole;
import kr.co.fitzcode.common.service.CustomUserDetails;
import kr.co.fitzcode.common.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/getUserByNickname")
    public ResponseEntity<UserDTO> userRoles(Model model) {
        UserDTO user  = userService.getUserByNickname("admin");
        return ResponseEntity.ok(user);
    }

    @GetMapping("getUserRolesById")
    public ResponseEntity<List<Integer>> userRolesById(Model model) {
        List<Integer> roles = userService.getUserRolesByUserId(1);
        return ResponseEntity.ok(roles);
    }

    @GetMapping("getUserRolesInStringByUserId")
    public ResponseEntity<List<UserRole>> userRolesInStringByUserId(Model model) {
        List<UserRole> roles = userService.findRolesInStringByUserId(1);
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/getCurrentUserId")
    public ResponseEntity<Integer> getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            return ResponseEntity.ok(userDetails.getUserId());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }




}
