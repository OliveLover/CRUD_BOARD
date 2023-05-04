package com.example.crud.jwt;

import com.example.crud.dto.TokenDto;
import com.example.crud.dto.UserRole;
import com.example.crud.entity.RefreshToken;
import com.example.crud.repository.RefreshTokenRepository;
import com.example.crud.security.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    //public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 권한 값의 KEY
    //public static final String AUTHORIZATION_KEY = "auth";
    private static final String BEARER_PREFIX = "Bearer ";
    //private static final long TOKEN_TIME = 60 * 60 * 1000L;
    private static final long ACCESS_TIME = 60 * 1000L;
    private static final long REFRESH_TIME = 7 * 24 * 60 * 60 * 1000L;
    public static final String ACCESS_TOKEN = "Access_Token";
    public static final String REFRESH_TOKEN = "Refresh_Token";

    private final UserDetailsServiceImpl userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

     //header 토큰을 가져오기
    public String resolveToken(HttpServletRequest request, String tokenType) {
        String token = tokenType.equals("Access_Token") ? ACCESS_TOKEN : REFRESH_TOKEN;
        String bearerToken = request.getHeader(token);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            //return bearerToken.substring(7);
            return bearerToken.split(" ")[1].trim();
        }
        return null;
    }

    // 토큰 생성
    public String createToken(String username, String tokenType) {
        Date date = new Date();

        long time = tokenType.equals("Access") ? ACCESS_TIME : REFRESH_TIME;

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username)
                        .setExpiration(new Date(date.getTime() + time))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    public Boolean refreshTokenValidation(String token) {

        //1차 토큰 검증
        if(!validateToken(token)) return false;

        //DB에 저장한 토큰 비교
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUsername(getUserInfoFromToken(token));

        //return refreshToken.isPresent() && token.equals(refreshToken.get().getRefreshToken().substring(7));
        return refreshToken.isPresent() && token.equals(refreshToken.get().getRefreshToken().split(" ")[1].trim());
    }

    // 토큰에서 사용자 정보 가져오기
    public String getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    // 인증 객체 생성
    public Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    //토큰 생성
    public TokenDto createAllToken(String username ) {
        return new TokenDto(createToken(username, "Access"), createToken(username, "Refresh"));
    }

    //Access token 헤더 설정
    public void setHeaderAccessToken(HttpServletResponse httpServletResponse, String accessToken) {
        httpServletResponse.setHeader("Access_Token", accessToken);
    }

    //Refresh token 헤더 설정
    public void setHeaderRefreshToken(HttpServletResponse httpServletResponse, String refreshToken) {
        httpServletResponse.setHeader("Refresh_Token", refreshToken);
    }
}