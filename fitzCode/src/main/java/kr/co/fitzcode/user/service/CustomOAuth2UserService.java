package kr.co.fitzcode.user.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.fitzcode.common.dto.CustomOAuth2User;
import kr.co.fitzcode.common.dto.NaverResponse;
import kr.co.fitzcode.common.dto.UserDTO;
import kr.co.fitzcode.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserMapper userMapper;
    private final HttpServletRequest request;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println("oAuth2User >>>>>>>>>>>>>>>>>>>>>>>" + oAuth2User);
        // 어떤 정보가 넘어 오는지 확인
        String registerId = userRequest.getClientRegistration().getRegistrationId();
        System.out.println("registerId >>>>>>>>>>>>>>>>>>>>>" + registerId);

        OAuth2Response oAuth2Response = null;

        // 여기에 카카오 추가
        if (registerId.equals("naver")) {
            // 네이버 응답
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        // 로그인한 회원 데이터베이스에 추가!!!!!!!!!!!!!!!!!!!!!!!

        // 기존에 있는 사용자라면 데이터 갱신, 신규 사용자라면 데이터 저장

        // 정보 제공자 + 제공자가 주는 아이디 로 사용자를 구분
        // Ex_ naver_eladfkj135, google_1344659012
        String userId = oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId();
        // db 에 이런 회원이 존재하는 확인

        // 나는 이거 findByUsername 매퍼에 sql 문 적어야됨 ㅇㅇ
        UserDTO user = userMapper.findByUsername(userId);

        HttpSession session = request.getSession();


        // 역할 주기
        // 이게  role_id
        // null 로 주고 신규 사용자는 무조건 1
        int role = 1;
        String birth = oAuth2Response.getBirthyear() + "-" + oAuth2Response.getBirthday();

        // 신규 사용자라면 db 에 데이터 저장
        if (user == null) {
            UserDTO user1 = new UserDTO();
            user1.setNaverId(userId);
            user1.setUserName(oAuth2Response.getuserName());
            user1.setNickname(oAuth2Response.getNickname());
            user1.setEmail(oAuth2Response.getEmail());
            user1.setPhoneNumber(oAuth2Response.getPhoneNumber());
            user1.setBirthDate(birth);
            user1.setProfileImage(oAuth2Response.getProfileImageUrl());
            user1.setRoleId(1);

            userMapper.insertUser(user1);

            // 세션에 사용자 정보 저장
            session.setAttribute("dto", user1);
        } else {
            if (user.getEmail().equals(oAuth2Response.getEmail())) {
                throw new IllegalStateException("이미 등록된 이메일입니다. 로그인해주세요.");
            } else {
                // 기존 사용자가 아닌 경우 이메일 갱신
                user.setEmail(oAuth2Response.getEmail());
                userMapper.updateUser(user);

                session.setAttribute("dto", user);

            }
        }




        return new CustomOAuth2User(oAuth2Response, role);

    }
}
