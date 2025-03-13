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

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserMapper userMapper;
    private final HttpServletRequest request;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registerId = userRequest.getClientRegistration().getRegistrationId();
        System.out.println("registerId >>>>>>>>>>>>>>>>>>>>>" + registerId);

        HttpSession session = request.getSession();

        OAuth2Response oAuth2Response;
        String providerUserId;
        String userBirth;

        if (registerId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else if (registerId.equals("kakao")) {
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        providerUserId = oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId();
        userBirth = oAuth2Response.getBirthyear() + "-" + oAuth2Response.getBirthday();

        UserDTO user = registerId.equals("naver") ? userMapper.findByUserNaverId(providerUserId) : userMapper.findByUserKakaoId(providerUserId);

        int dbUserId;
        String userRole;
        if (user == null) {
            UserDTO newUser = new UserDTO();
            if (registerId.equals("naver")) {
                newUser.setNaverId(providerUserId);
            } else if (registerId.equals("kakao")) {
                newUser.setKakaoId(providerUserId);
            }
            newUser.setUserName(oAuth2Response.getuserName());
            newUser.setNickname(oAuth2Response.getNickname());
            newUser.setEmail(oAuth2Response.getEmail());
            newUser.setPhoneNumber(oAuth2Response.getPhoneNumber());
            newUser.setBirthDate(userBirth);
            newUser.setProfileImage(oAuth2Response.getProfileImageUrl());
            newUser.setRoleId(1);

            userMapper.insertUser(newUser);
            user = userMapper.findByEmail(newUser.getEmail());
            dbUserId = user.getUserId();
            userRole = "ROLE_USER";
            session.setAttribute("dto", user);
        } else {
            dbUserId = user.getUserId();
            userRole = user.getRoleId() == 2 ? "ROLE_ADMIN" : "ROLE_USER";
            session.setAttribute("dto", user);
        }

        System.out.println("Returning CustomOAuth2User with userId=" + dbUserId + ", role=" + userRole);
        return new CustomOAuth2User(oAuth2Response, userRole, dbUserId);
    }
}