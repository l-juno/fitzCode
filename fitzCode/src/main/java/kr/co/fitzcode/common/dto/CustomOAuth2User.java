package kr.co.fitzcode.common.dto;

import kr.co.fitzcode.user.service.OAuth2Response;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

public class CustomOAuth2User implements OAuth2User {
    private final OAuth2Response oAuth2Response;
    private final String role;
    private final int userId;

    public CustomOAuth2User(OAuth2Response oAuth2Response, String role, int userId) {
        this.oAuth2Response = oAuth2Response;
        this.role = role;
        this.userId = userId;
    }

    @Override
    public Map<String, Object> getAttributes() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("birthyear", oAuth2Response.getBirthyear());
        attributes.put("birthday", oAuth2Response.getBirthday());
        attributes.put("email", oAuth2Response.getEmail());
        attributes.put("phonenumber", oAuth2Response.getPhoneNumber());
        attributes.put("username", oAuth2Response.getuserName());
        attributes.put("profileimg", oAuth2Response.getProfileImageUrl());
        attributes.put("provider", oAuth2Response.getProvider());
        attributes.put("providerid", oAuth2Response.getProviderId());
        attributes.put("nickname", oAuth2Response.getNickname());
        attributes.put("userId", userId);
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));
        return authorities;
    }

    @Override
    public String getName() {
        return oAuth2Response.getuserName();
    }

    public String getUsername() {
        return oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId();
    }

    public int getUserId() {
        return userId;
    }
}