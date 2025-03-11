package kr.co.fitzcode.common.dto;

import kr.co.fitzcode.user.service.OAuth2Response;

import java.util.Map;

public class KakaoResponse implements OAuth2Response {
    private final Map<String, Object> attributes;

    public KakaoResponse(Map<String, Object> attributes) {
        this.attributes = attributes;
    }


    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("aaccount_email").toString();
    }

    @Override
    public String getuserName() {
        return attributes.get("name").toString();
    }

    @Override
    public String getNickname() {
        return attributes.get("profile_nickname").toString();
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
        return attributes.get("phone_number").toString();
    }

    @Override
    public String getProfileImageUrl() {
        return attributes.get("profile_image").toString();
    }
}
