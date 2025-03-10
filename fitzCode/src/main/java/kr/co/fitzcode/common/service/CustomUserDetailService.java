package kr.co.fitzcode.common.service;

import kr.co.fitzcode.common.dto.UserDTO;
import kr.co.fitzcode.common.enums.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // get userDTO using email (what user uses to login)
        log.debug("Attempting to load user by email: {}", email);

        UserDTO user = userService.getUserByEmail(email);
        if (user == null) {
            log.warn("User not found with email: {}", email);
            throw new UsernameNotFoundException("User not found: " + email);
        }

        log.debug("User found: {}", user);

        // get the roles of this user using the userDTO
        List<UserRole> roles = userService.findRolesInStringByUserId(user.getUserId());
        log.debug("Loaded roles for user with email {}: {}", email, roles);

        // Use the authorities directly from UserRole (which implements GrantedAuthority)
        List<GrantedAuthority> authorities = roles.stream()
                .peek(role -> log.debug("Granting authority: {}", role.getAuthority()))
                .map(GrantedAuthority.class::cast)
                .toList();

        log.debug("Returning UserDetails with authorities: {}", authorities);

        return new CustomUserDetails(user, authorities);
    }
}