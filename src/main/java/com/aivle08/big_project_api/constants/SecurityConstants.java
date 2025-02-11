package com.aivle08.big_project_api.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SecurityConstants {

    public static String JWT_KEY;
    public static final String JWT_HEADER = "Authorization";

    @Value("${jwt.secret}")
    public void setJwtKey(String jwtKey) {
        JWT_KEY = jwtKey;
    }
}