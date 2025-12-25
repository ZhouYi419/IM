package com.zy.im.common;

import com.zy.im.common.security.JwtUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
public class JwtUtil {

    private static final String SECRET =
            "o2FZP4xv3F6vY3k5Uu9K9e7qkL1+2W7QmQxYpZJx5pE=";

    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000L; // 24h

    private static final SecretKey KEY =
            Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    private JwtUtil() {}

    /**
     * 生成 Token
     */
    public static String generateToken(Long userId, String username) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("username", username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 解析 Claims
     */
    public static JwtUser parseToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String userId = claims.getSubject();
            log.info("获取到userId:{}",userId);
            String username = claims.get("username", String.class);
            return new JwtUser(Long.valueOf(userId), username);
        } catch (JwtException e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    /** 获取 userId */
    public static Long getUserId(String token) {
        return parseToken(token).getUserId();
    }

    /** 获取 username */
    public static String getUsername(String token) {
        return parseToken(token).getUsername();
    }
}
