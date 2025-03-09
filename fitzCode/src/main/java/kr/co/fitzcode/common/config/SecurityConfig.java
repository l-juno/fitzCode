package kr.co.fitzcode.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.sendRedirect("/access-denied");
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // url mapping
        http
                .authorizeHttpRequests(authorizeRequests -> {
                    authorizeRequests
                            // any user can access this url
                            // 모든 유저가 접근할수있는 url
                            .requestMatchers("/", "/css/**", "/js/**", "/img/**","/access-denied", "/404-error",
                                    // 도연 related


                                    // 세진 related
                                    "/something",

                                    // 준호
                                    "/product/list/**", "/product/detail/**", "/api/product/**"

                                    // 형윤 (ADMIN)

                            )
                            .permitAll()

                            // ADMIN, LOGISTICS, INQUIRY가 접근 가능한 대시보드
                            .requestMatchers("/admin/dashboard") // products, notice 제외
                            .hasAnyRole("ADMIN", "LOGISTICS", "INQUIRY")

                            // 물류 담당자 (LOGISTICS) 접근 가능 경로
                            .requestMatchers(
                                    "/admin/products", // 목록
                                    "/admin/products/*", // add, list 등
                                    "/admin/sales",
                                    "/admin/sales/*",
                                    "/admin/products/returns",
                                    "/admin/products/returns/*",
                                    "/admin/products/refund",
                                    "/admin/products/refund/*",
                                    "/admin/shipping",
                                    "/admin/shipping/*")
                            .hasRole("LOGISTICS")

                            // 문의 담당자 (INQUIRY) 접근 가능 경로
                            .requestMatchers(
                                    "/admin/inquiries",
                                    "/admin/inquiries/*",
                                    "/admin/products/qna/{productId}")
                            .hasRole("INQUIRY")

                            // notice 관련 경로 (ADMIN 또는 LOGISTICS로 이동)
                            .requestMatchers("/admin/notice", "/admin/notice/*") // notice/add 포함, 슬래시 추가
                            .hasAnyRole("ADMIN", "LOGISTICS") // INQUIRY 제외

                            // products/{productId}는 ADMIN 또는 LOGISTICS로 이동
                            .requestMatchers("/admin/products/{productId}", "/admin/products/{productId}/**") // 하위 경로 포함
                            .hasAnyRole("ADMIN", "LOGISTICS")

                            // only those with role admin can approach this url
                            // 어드민 유저가 접근할수있는 url
                            .requestMatchers("/admin/**").hasRole("ADMIN")

                            // only those with role user can approach this url (where user needs to login to approach)
                            // 유저가 접근할수있는 url, 로그인이 필요한 url
                            .requestMatchers("/cart/**").hasRole("USER")

                            // all other requests need authentication
                            // 나머지... authentication 필요
                            .anyRequest().authenticated();
                });

        // Enable form login (default Spring login page)
        http.formLogin(withDefaults());

        // 우리가 만든 로그인
        // login
        http.formLogin(formLogin -> {
            formLogin.loginPage("/login")                // set login page URL
                    .loginProcessingUrl("/loginValidate") // login validation page URL
                    .usernameParameter("email")          // email
                    .passwordParameter("password")       // password
                    .defaultSuccessUrl("/")              // where to go upon success
                    .permitAll();                        // allow all users to access login page
        });

        // 403 처리
        http.exceptionHandling(exceptionHandling ->
                exceptionHandling.accessDeniedHandler(accessDeniedHandler())
        );

        // logout
        http.logout(logout -> {
            logout.logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll();
        });

        http.csrf(auth -> auth.disable());

        return http.build();
    }
}