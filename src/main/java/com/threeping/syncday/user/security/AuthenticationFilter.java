package com.threeping.syncday.user.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.threeping.syncday.common.ResponseDTO;
import com.threeping.syncday.user.command.domain.vo.LoginRequestVO;
import com.threeping.syncday.user.command.domain.vo.ResponseNormalLoginVO;
import com.threeping.syncday.user.query.dto.UserDTO;
import com.threeping.syncday.user.query.service.UserQueryService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final UserQueryService userService;
    private final Environment env;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                UserQueryService userService,
                                Environment env,
                                BCryptPasswordEncoder bCryptPasswordEncoder) {
        super(authenticationManager);
        this.userService = userService;
        this.env = env;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        try {
            LoginRequestVO creds = new ObjectMapper().readValue(request.getInputStream(), LoginRequestVO.class);
            String email = creds.getEmil();

            // 인증 token 만들기
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(email, creds.getPassword(), new ArrayList<>())
            );
        } catch (IOException e) {
            throw new AuthenticationServiceException("유저 인증 실패", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        log.info("successfulAuthentication! Principal 객체 : {}", authResult);

        String email = ((User) authResult.getPrincipal()).getUsername();

        Claims claims = Jwts.claims().setSubject(email); // 회원 정보
        List<String> roles = authResult.getAuthorities().stream()
                .map(role -> role.getAuthority())
                .collect(Collectors.toList()); // 권한 정보
        claims.put("auth", roles);

        // 토큰 만료 시간 설정
        long accessExpiration =
                System.currentTimeMillis() + getExpirationTime(env.getProperty("token.access-expiration-time"));
        // refresh는 추후에 설정할 예정
        long refreshExpiration =
                System.currentTimeMillis() + getExpirationTime(env.getProperty("token.refresh-expiration-time"));

        // AT 생성
        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(accessExpiration))
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
                .compact();

        // RT 생성
        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(refreshExpiration))
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
                .compact();

        // user 상세 정보 조회
        UserDTO userDetails = userService.findByEmail(email);

        ResponseNormalLoginVO responseNormalLoginVO = new ResponseNormalLoginVO(
                accessToken,
                new Date(accessExpiration),
                refreshToken,
                new Date(refreshExpiration),
                userDetails.getUserId(),
                userDetails.getUserName(),
                userDetails.getEmail(),
                userDetails.getProfilePhoto(),
                userDetails.getJoinYear(),
                userDetails.getPosition(),
                userDetails.getTeamId()
        );

        ResponseDTO<ResponseNormalLoginVO> responseDTO = ResponseDTO.ok(responseNormalLoginVO);

        //JSON 문자열로 변환
        String JSON = new ObjectMapper().writeValueAsString(responseDTO);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(JSON);
    }

    private long getExpirationTime(String expirationTime) {
        if (expirationTime == null) {
            // 주어진 값이 없을 때 기본 시간 설정
            return 3600000;
        }
        return Long.parseLong(expirationTime);
    }
}