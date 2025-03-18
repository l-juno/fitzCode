package kr.co.fitzcode.common.dto;

import kr.co.fitzcode.user.service.OAuth2Response;

import java.util.Map;
import java.util.Random;

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
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        return kakaoAccount != null ? kakaoAccount.get("email").toString() : null;
    }

    @Override
    public String getuserName() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        return kakaoAccount != null ? kakaoAccount.get("name").toString() : null;
    }

    @Override
    public String getNickname() {
        // 카카오 계정에서 프로필 정보 가져오기
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = kakaoAccount != null ? (Map<String, Object>) kakaoAccount.get("profile") : null;

        String nickname = profile != null ? profile.get("nickname").toString() : null;

        if (nickname != null) {
            nickname = nickname + "_" + rndNickname();
        }

        return nickname;
    }

    @Override
    public String getBirthday() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        String birthday = kakaoAccount != null ? kakaoAccount.get("birthday").toString() : null;

        // 생일 MM-DD로 변환
        if (birthday != null && birthday.length() == 4) {
            return birthday.substring(0, 2) + "-" + birthday.substring(2);
        }
        return null;
    }

    @Override
    public String getBirthyear() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        return kakaoAccount != null ? kakaoAccount.get("birthyear").toString() : null;
    }

    @Override
    public String getPhoneNumber() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        if (kakaoAccount != null && kakaoAccount.get("phone_number") != null) {
            String phoneNumber = kakaoAccount.get("phone_number").toString();
            System.out.println("카카오 기존 전화번호: " + phoneNumber);

            String formattedPhoneNumber = phoneNumber.replace("+82 10", "010");

            System.out.println("변환 전화번호: " + formattedPhoneNumber);
            return formattedPhoneNumber;
        }
        return null;
    }

    @Override
    public String getProfileImageUrl() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = kakaoAccount != null ? (Map<String, Object>) kakaoAccount.get("profile") : null;
        return profile != null ? profile.get("profile_image_url").toString() : null;
    }

    private int rndNickname(){
        Random random = new Random();
        return random.nextInt(90000) + 10000;
    }
}