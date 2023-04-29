package com.example.crud.config;

import com.example.crud.jwt.JwtAuthFilter;
import com.example.crud.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration                                                                                                 //Bean등록을 해주며 싱글톤으로 유지해준다.
@RequiredArgsConstructor
@EnableWebSecurity                                                                                       //웹 보안 활성화

/*
 @EnableGlobalMethodSecurity(securedEnabled = true)
  -> @EnableMethodSecurity로 대체

  link : https://docs.spring.io/spring-security/reference/servlet/authorization/method-security.html

 */

@EnableMethodSecurity(jsr250Enabled = true)
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;

    private static final String[] PERMIT_URL_ARRAY = {
            /* swagger v2 */
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            /* swagger v3 */
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {                                              //Spring Security에서 암호 인코더 빈을 생성
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        //h2-console 사용 및 resources 접근 허용 설정
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console())
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());

        /*
        WebSecurityCustomizer 인터페이스를 구현하여
        requestMatchers() 메서드로 무시할 경로를 지정
         */
    }

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers(PERMIT_URL_ARRAY).permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/posts").permitAll()
                .anyRequest().authenticated().and().addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        /*
        기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한설정
        STATELESS : 이서버에서는 더이상 세션을 생성하지 않으며, 클라이언트와의 각 요청마다
        인증 정보를 새롭게 보내 인증을 유지하는 방식을 사용하겠다는 것을 의미
         */
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        /*
        HTTP 오청에 대한 권한 부여 설정
        "api/**" 패턴의 URL 요청에 대해서는 모든 권한을 허용하고, 나머지 요청에 대해서는 인증된 사용자만
        접근 할 수 있도록 설정, JWT 인증 필터를 추가.

        UsernamePasswordAuthenticationFilter : JWT 토큰이 유효한지 확인한뒤에 유효하다면 해당 토큰에 저장된 사용자 정보를
        이용하여 인증된 사용자인지를 판단.
         */

//        http.authorizeHttpRequests().requestMatchers("api/auth/**").permitAll()
//                .requestMatchers("api/posts").permitAll()
//                .anyRequest().authenticated().and().addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
