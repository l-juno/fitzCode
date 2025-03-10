package kr.co.fitzcode.common.dto;

import kr.co.fitzcode.user.service.OAuth2Response;

import java.util.Map;


public class NaverResponse implements OAuth2Response {

    private final Map<String, Object> attributes;

    public NaverResponse(Map<String, Object> attributes) {
        this.attributes = (Map<String, Object>)attributes.get("response");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getuserName() {
        return attributes.get("name").toString();
    }

    @Override
    public String getNickname() {
        return attributes.get("nickname").toString();
    }

    @Override
    public String getBirthday() {
        return attributes.get("birthday").toString();
    }

    @Override
    public String getBirthyear() {
        return attributes.get("birthyear").toString();
    }


    @Override
    public String getPhoneNumber() {
        return attributes.get("mobile").toString();
    }

    @Override
    public String getProfileImageUrl() {
        return attributes.get("profile_image").toString();
    }
}
