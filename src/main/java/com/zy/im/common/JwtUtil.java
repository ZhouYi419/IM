package com.zy.im.common;

import com.zy.im.common.exception.BusinessException;
import com.zy.im.common.exception.ErrorCode;
import com.zy.im.common.security.JwtUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

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
    public static String generateToken(String uuid, String username) {
        return Jwts.builder()
                .setSubject(uuid)
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

            String uuid = claims.getSubject();
            log.info("获取到uuid:{}",uuid);
            String username = claims.get("username", String.class);
            return new JwtUser(username,uuid);
        } catch (JwtException e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    /** 获取 uuid */
    public static String getUserId(String token) {
        return parseToken(token).getUuid();
    }

    /** 获取 username */
    public static String getUsername(String token) {
        return parseToken(token).getUsername();
    }

    /**
     * 获取过期时间
     * @param token token
     * @return 过期时间
     */
    public static Long getExpiration(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getExpiration().getTime();  // 返回过期时间的毫秒数
        } catch (JwtException e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    /**
     * 获取请求头Token
     * @return token
     */
    public static String getToken(WebSocketSession session){
        String auth = session.getHandshakeHeaders().getFirst("Authorization");
        if (auth != null && auth.startsWith("Bearer ")){
            return auth.substring(7);
        }

        throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
    }
}