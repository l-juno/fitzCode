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
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {

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
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityContext(securityContext ->
                        securityContext.securityContextRepository(new HttpSessionSecurityContextRepository())
                )
                .authorizeHttpRequests(authorizeRequests -> {
                    // permitAll() 경로를 가장 먼저 명시
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
                                    "/inquiry/selectedProduct",
                                    "/admin/notice/subscribe",
                                    "/api/notifications",
                                    "/products",
                                    "/styles",
                                    "/notice",
                                    "/api/cart/**",
                                    "/api/user/check",
                                    "/admin/notice/subscribe"
                            ).permitAll()
                            // 권한별 경로
                            .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
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
                            .logoutSuccessUrl("/")
                            .invalidateHttpSession(true)
                            .clearAuthentication(true)
                            .deleteCookies("JSESSIONID")
                            .permitAll();
                })
                .sessionManagement(session -> {
                    session
                            .sessionFixation().migrateSession()
                            .maximumSessions(1)
                            .maxSessionsPreventsLogin(true)
                            .expiredUrl("/login?expired");
                })
                .exceptionHandling(exceptionHandling -> {
                    exceptionHandling
                            .authenticationEntryPoint((request, response, authException) -> {
                                String requestUri = request.getRequestURI();
                                // permitAll() 경로면 리디렉션 안할거임
                                if (requestUri.equals("/") || requestUri.equals("/login") || requestUri.equals("/logout") ||
                                        requestUri.startsWith("/css/") || requestUri.startsWith("/js/") ||
                                        requestUri.startsWith("/img/") ||
                                        requestUri.startsWith("/product/list/") || requestUri.startsWith("/product/detail/") ||
                                        requestUri.startsWith("/api/product/") || requestUri.equals("/joinForm") ||
                                        requestUri.equals("/joinSuccess") || requestUri.equals("/pwEmail") ||
                                        requestUri.equals("/findEmail") || requestUri.equals("/findEmailSuccess") ||
                                        requestUri.equals("/resetPw") || requestUri.equals("/resetPwSuccess") ||
                                        requestUri.equals("/inquiry/searchProduct") || requestUri.equals("/inquiry/searchOrderList") ||
                                        requestUri.equals("/inquiry/selectedProduct") || requestUri.equals("/admin/notice/subscribe") ||
                                        requestUri.equals("/api/notifications") || requestUri.equals("/products") ||
                                        requestUri.equals("/styles") || requestUri.equals("/notice") ||
                                        requestUri.startsWith("/api/cart/")) {
                                    return;
                                }
                                response.sendRedirect("/login");
                            });
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