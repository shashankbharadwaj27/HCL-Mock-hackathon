package com.example.backend.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;

@Component
@ConfigurationProperties(prefix = "app.cookie")
@Getter @Setter
public class CookieConfig {

    private boolean secure;
    private String sameSite;

    public Cookie buildJwtCookie(String token) {
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(secure);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60); // 24 hours — matches JWT expiry
        return cookie;
    }

    public Cookie buildExpiredJwtCookie() {
        Cookie cookie = new Cookie("jwt", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(secure);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        return cookie;
    }

    public Cookie clearJwtCookie() {
        return buildExpiredJwtCookie();  // delegate — same behavior
    }
}