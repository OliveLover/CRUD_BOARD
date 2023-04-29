package com.example.crud.jwt;

import com.example.crud.dto.SecurityExceptionDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        String accessToken = jwtUtil.resolveToken(request, JwtUtil.ACCESS_TOKEN);
        String refreshToken = jwtUtil.resolveToken(request, JwtUtil.REFRESH_TOKEN);

        if (accessToken != null) {
            //Access Token이 유효하면 setAuthentication을 통해 Security context에 인증정보 저장
            if (jwtUtil.validateToken(accessToken)) {
                setAuthentication(jwtUtil.getUserInfoFromToken(accessToken));
            } else if (refreshToken != null) {
                boolean isRefreshToken = jwtUtil.refreshTokenValidation(refreshToken);
                //리프레시 토큰이 유효하고 리프레시 토큰이 DB와 비교했을 때 똑같다면
                if (isRefreshToken) {
                    //Refresh Token으로 아이디 정보 가져오기
                    String loginId = jwtUtil.getUserInfoFromToken(refreshToken);
                    //새로운 Access Token 발급
                    String newAccessToken = jwtUtil.createToken(loginId, "Access");
                    //헤더에 Access Token 추가
                    jwtUtil.setHeaderAccessToken(response, newAccessToken);
                    //Security context에 인증정보 넣기
                    setAuthentication(jwtUtil.getUserInfoFromToken(newAccessToken));;
                }
            else {
                jwtExceptionHandler(response, "RefreshToken Expired", 400);
                return;
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    public void setAuthentication(String username) {
        Authentication authentication = jwtUtil.createAuthentication(username);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void jwtExceptionHandler(HttpServletResponse response, String msg, int statusCode) {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        try {
            String json = new ObjectMapper().writeValueAsString(new SecurityExceptionDto(statusCode, msg));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}