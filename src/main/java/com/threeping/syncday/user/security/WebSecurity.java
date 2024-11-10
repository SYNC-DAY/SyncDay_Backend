package com.threeping.syncday.user.security;

import com.threeping.syncday.user.query.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurity {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserService userService;
    private final Environment environment;

    @Autowired
    public WebSecurity(BCryptPasswordEncoder bCryptPasswordEncoder,
                       UserService userService,
                       Environment environment) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userService = userService;
        this.environment = environment;
    }

    // 인가(Authorization) method, filter chain을 덧붙일 예정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // restAPI의 경우 JWT만 사용해서 Authorization Header에 토큰을 전송하는 방식만을 사용하되,(차후 restAPI는 왜 jwt만으로 충분한지 공부)
        // Https(우리 서비스의 경우 ACM을 통한 https 통신이 가능하므로)와 토큰 정책을 통한 보안만으로 충분하다고 판단
        // 따라서 csrf token 방식은 사용하지 않기로 정함
        http.csrf((csrf) -> csrf.disable());

        // 로그인 시, http에 추가될 authenticationManager
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        // userDetails 클래스를 물려받은 우리 서비스 고유의 userService인식 + 암호화 방식 인식
        authenticationManagerBuilder.userDetailsService(userService)
                .passwordEncoder(bCryptPasswordEncoder);

        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        // 회원가입만 열기
        http.authorizeHttpRequests((auth) ->
                auth.requestMatchers(new AntPathRequestMatcher("/api/user/regist")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/user/health")).permitAll()
                        .anyRequest().authenticated()
        )
                // manager 등록
                .authenticationManager(authenticationManager)
                // session 방식 사용 x
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    // 인증(Authentication) method, filter를 반환하는 메서드
}