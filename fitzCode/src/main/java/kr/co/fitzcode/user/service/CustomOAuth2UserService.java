package kr.co.fitzcode.user.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.fitzcode.common.dto.CustomOAuth2User;
import kr.co.fitzcode.common.dto.KakaoResponse;
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

        HttpSession session = request.getSession();

        OAuth2Response oAuth2Response = null;
        String userIdNaver = null;
        String userIdKakao = null;
        String userBirthNaver = null;
        String userBirthKakao = null;

        // 여기에 카카오 추가
        if (registerId.equals("naver")) {
            // 네이버 응답
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
            userIdNaver = oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId();
            userBirthNaver = oAuth2Response.getBirthyear() + "-" + oAuth2Response.getBirthday();
            System.out.println("Kakao Response:::::::::::::::::::::::::: " + oAuth2User.getAttributes());

        } else if (registerId.equals("kakao")) {
            // 카카오 응답
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
            userIdKakao = oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId();
            userBirthKakao = oAuth2Response.getBirthyear() + "-" + oAuth2Response.getBirthday();

        } else {
            return null;
        }


        // 기존에 있는 사용자라면 데이터 갱신, 신규 사용자라면 데이터 저장

        // 정보 제공자 + 제공자가 주는 아이디 로 사용자를 구분
        // Ex_ naver_eladfkj135, google_1344659012
//        String userId = oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId();
        // db 에 이런 회원이 존재하는 확인

        // 네이버 고유 아이디로 해당 유저가 있는지 화긴
        UserDTO usernaver = userMapper.findByUserNaverId(userIdNaver);

        // 카카오
        UserDTO userkakao = userMapper.findByUserKakaoId(userIdKakao);



        // 역할 주기
        // 이게  role_id
        // null 로 주고 신규 사용자는 무조건 1
        int role = 1;

        // 신규 사용자라면 db 에 데이터 저장
        if (usernaver == null) {
            UserDTO newUserNaver = new UserDTO();
            newUserNaver.setNaverId(userIdNaver);
            newUserNaver.setUserName(oAuth2Response.getuserName());
            newUserNaver.setNickname(oAuth2Response.getNickname());
            newUserNaver.setEmail(oAuth2Response.getEmail());
            newUserNaver.setPhoneNumber(oAuth2Response.getPhoneNumber());
            newUserNaver.setBirthDate(userBirthNaver);
            newUserNaver.setProfileImage(oAuth2Response.getProfileImageUrl());
            newUserNaver.setRoleId(1);

            userMapper.insertUser(newUserNaver);

            // 네이버 신규 사용자 로그인 처리
            session.setAttribute("dto", newUserNaver);

        } else if(userkakao == null){
            UserDTO newUserKakao = new UserDTO();
            newUserKakao.setKakaoId(userIdKakao);
            newUserKakao.setUserName(oAuth2Response.getuserName());
            newUserKakao.setNickname(oAuth2Response.getNickname());
            newUserKakao.setEmail(oAuth2Response.getEmail());
            newUserKakao.setPhoneNumber(oAuth2Response.getPhoneNumber());
            newUserKakao.setBirthDate(userBirthKakao);
            newUserKakao.setProfileImage(oAuth2Response.getProfileImageUrl());
            newUserKakao.setRoleId(1);

            // 카카오 신규 사용자 로그인 처리
            session.setAttribute("dto", newUserKakao);
        }else {
            // 기존 사용자 로그인 처리
            session.setAttribute("dto", usernaver);
        }

        return new CustomOAuth2User(oAuth2Response, role);

    }
}
