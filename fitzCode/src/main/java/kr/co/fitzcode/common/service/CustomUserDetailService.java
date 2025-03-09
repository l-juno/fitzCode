package kr.co.fitzcode.common.service;

import kr.co.fitzcode.common.dto.UserDTO;
import kr.co.fitzcode.common.enums.UserRole;
import kr.co.fitzcode.common.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserService userService;


    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        // get userDTO using nickname (what user uses to login)
        UserDTO user = userService.getUserByNickname(nickname);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + nickname);
        }

        // get the roles of this user using the userDTO
        List<UserRole> roles = userService.findRolesInStringByUserId(user.getUserId());

        // translate the user roles into authorities for spring security to track
        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList());

        return new CustomUserDetails(   // user class for spring security
                user,                   // userDTO
                authorities             // Authorities (Roles)
        );
    }
}
