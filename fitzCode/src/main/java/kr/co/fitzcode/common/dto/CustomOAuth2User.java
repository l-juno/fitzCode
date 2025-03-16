package kr.co.fitzcode.common.dto;

import kr.co.fitzcode.user.service.OAuth2Response;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final OAuth2Response oAuth2Response;
    private final String role;

    public CustomOAuth2User(OAuth2Response oAuth2Response, String role) {
        this.oAuth2Response = oAuth2Response;
        this.role = String.valueOf(role);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));

        return authorities;
    }

    @Override
    public String getName() {
        return oAuth2Response.getuserName();
    }
    public  String getUsername() {
        return oAuth2Response.getProvider()+"_"+oAuth2Response.getProviderId() ;
    }
}
