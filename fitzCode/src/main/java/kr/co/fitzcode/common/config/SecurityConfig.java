package kr.co.fitzcode.common.config;

import jakarta.servlet.http.HttpServletResponse;
import kr.co.fitzcode.user.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> response.sendRedirect("/access-denied");
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            System.out.println("Login successful: " + authentication.getName());
            response.sendRedirect("/");
        };
    }

    @Bean
    public AuthenticationFailureHandler failureHandler() {
        return (request, response, exception) -> {
            System.out.println("Login failed: " + exception.getMessage());
            response.sendRedirect("/login?error=true");
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityContext(securityContext ->
                        securityContext.securityContextRepository(new HttpSessionSecurityContextRepository())
                )
                .authorizeHttpRequests(authorizeRequests -> {
                    authorizeRequests
                            .requestMatchers(
                                    "/",
                                    "/css/**",
                                    "/js/**",
                                    "/img/**",
                                    "/access-denied",
                                    "/404-error",
                                    "/something",
                                    "/product/list/**",
                                    "/product/detail/**",
                                    "/api/product/**",
                                    "/login/**",
                                    "/logout",
                                    "/joinForm",
                                    "/joinSuccess",
                                    "/pwEmail",
                                    "/findEmail",
                                    "/findEmailSuccess",
                                    "/resetPw",
                                    "/resetPwSuccess",
                                    "/inquiry/searchProduct",
                                    "/inquiry/searchOrderList",
                                    "/inquiry/selectedProduct"
                            ).permitAll()

                            // 관리자 전용 페이지
                            .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")

                            // 특정 권한이 필요한 페이지
                            .requestMatchers("/admin/dashboard")
                            .hasAnyAuthority("ROLE_ADMIN", "ROLE_LOGISTICS", "ROLE_INQUIRY")

                            .requestMatchers(
                                    "/admin/products",
                                    "/admin/products/*",
                                    "/admin/sales",
                                    "/admin/sales/*",
                                    "/admin/products/returns",
                                    "/admin/products/returns/*",
                                    "/admin/products/refund",
                                    "/admin/products/refund/*",
                                    "/admin/shipping",
                                    "/admin/shipping/*"
                            ).hasAnyAuthority("ROLE_ADMIN", "ROLE_LOGISTICS")

                            .requestMatchers(
                                    "/admin/inquiries",
                                    "/admin/inquiries/*",
                                    "/admin/products/qna/{productId}"
                            ).hasAnyAuthority("ROLE_ADMIN", "ROLE_INQUIRY")

                            .requestMatchers("/admin/notice", "/admin/notice/*")
                            .hasAnyAuthority("ROLE_ADMIN", "ROLE_LOGISTICS")

                            .requestMatchers("/admin/products/{productId}", "/admin/products/{productId}/**")
                            .hasAnyAuthority("ROLE_ADMIN", "ROLE_LOGISTICS")

                            .requestMatchers("/api/cart/**").hasAuthority("ROLE_USER")
                            .anyRequest().authenticated();
                })
                .formLogin(formLogin -> {
                    formLogin
                            .loginPage("/login")
                            .loginProcessingUrl("/login")
                            .usernameParameter("email")
                            .passwordParameter("password")
                            .successHandler(successHandler())
                            .failureHandler(failureHandler())
                            .permitAll();
                })
                .logout(logout -> {
                    logout
                            .logoutUrl("/logout")
                            .logoutSuccessUrl("/login")
                            .invalidateHttpSession(true)
                            .clearAuthentication(true)
                            .deleteCookies("JSESSIONID")
                            .permitAll();
                })
                .csrf(auth -> auth.disable());

        http
                .oauth2Login(oauth2 ->
                        oauth2.loginPage("/login")
                                .defaultSuccessUrl("/", true)
                                .userInfoEndpoint(userInfoEndpointConfig ->
                                        userInfoEndpointConfig.userService(customOAuth2UserService))
                );

        return http.build();
    }
}