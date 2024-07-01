package com.fundingflex.common.util.jwt;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fundingflex.member.domain.dto.CustomUserDetails;
import com.fundingflex.member.domain.dto.MemberSigninRequest;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private final JWTUtil jwtUtil;
	

	
	public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/signin");
    }

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
	        throws AuthenticationException {
	    if ("GET".equalsIgnoreCase(request.getMethod())) {
	        // GET 요청 무시 또는 적절한 응답 반환
	        try {
	            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "GET method is not supported");
	            return null;
	        } catch (IOException e) {
	            throw new RuntimeException(e);
	        }
	    }

	    try {
	        MemberSigninRequest loginRequest = null;

	        if ("application/json".equals(request.getContentType())) {
	            // JSON 형식 처리
	            ObjectMapper objectMapper = new ObjectMapper();
	            if (request.getInputStream().available() == 0) {
	                log.error("Request body is empty");
	                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Request body is empty");
	                return null;
	            }
	            String requestBody = new String(request.getInputStream().readAllBytes());
	            log.info("Request Body: " + requestBody);
	            loginRequest = objectMapper.readValue(requestBody, MemberSigninRequest.class);

	        } else if ("application/x-www-form-urlencoded".equals(request.getContentType())) {
	            // x-www-form-urlencoded 형식 처리
	            String body = request.getReader().lines().collect(Collectors.joining("&"));
	            log.info("Request Body: " + body);
	            loginRequest = parseFormUrlencodedBody(body);
	        } else {
	            log.error("Unsupported content type: " + request.getContentType());
	            response.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Unsupported content type");
	            return null;
	        }

	        if (loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
	            log.error("Email or Password is empty");
	            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email or Password is empty");
	            return null;
	        }
	        
	     // URL 인코딩된 이메일 주소 디코딩
	        String decodedEmail = URLDecoder.decode(loginRequest.getEmail(), StandardCharsets.UTF_8.name());

	        log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%Attempting authentication for user: {}", loginRequest.getEmail());

	        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
	        		 decodedEmail, loginRequest.getPassword());

	        return authenticationManager.authenticate(authToken);

	    } catch (IOException e) {
	        throw new RuntimeException(e);
	    }
	}

	// 로그인 성공시 JWT를 발급
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,	
			Authentication authentication) throws IOException, ServletException {
		log.info("success / Authentication successful for user: " + authentication.getName());

		if (!(authentication.getPrincipal() instanceof CustomUserDetails)) {
	        throw new UsernameNotFoundException("User details not found");
	    }
		
		 CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
		    String email = customUserDetails.getUsername();
		    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		    String role = authorities.iterator().next().getAuthority();
		    String token = jwtUtil.createJwt(email, role, 3600000L);

		    // JWT 토큰을 쿠키에 설정합니다.
		    String encodedToken = Base64.getUrlEncoder().encodeToString(token.getBytes(StandardCharsets.UTF_8));
		    Cookie cookie = new Cookie("Authorization", encodedToken);
		    cookie.setHttpOnly(true);
		    cookie.setSecure(true);
		    cookie.setPath("/");
		    cookie.setMaxAge(3600); // 쿠키의 유효 기간을 설정합니다.

		    // 응답에 쿠키를 추가합니다.
		    response.addCookie(cookie);

		    SecurityContextHolder.getContext().setAuthentication(authentication);
		    response.sendRedirect("/");
		
//		// CustomUserDetails 객체를 가져옵니다. 이는 인증된 사용자 정보를 포함합니다.
//		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
//		// 사용자 이름을 CustomUserDetails 객체에서 가져옵니다.
//		String email = customUserDetails.getUsername(); // email
//		log.info("######################email" + email);
//		// 인증된 사용자의 권한(role)을 가져옵니다.
//		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//		Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
//		GrantedAuthority auth = iterator.next();
//		// 첫 번째 권한의 이름(역할)을 가져옵니다.
//		String role = auth.getAuthority();
//		// JWT 토큰을 생성합니다. 토큰은 사용자 이름과 역할, 유효 기간을 포함합니다.
//		String token = jwtUtil.createJwt(email, role, 60 * 60 * 10L); // 10시간
//		// 생성된 JWT 토큰을 HTTP 응답 헤더에 추가합니다.
////		response.addHeader("Authorization", "Bearer " + token);
//		
//		String encodedToken = Base64.getEncoder().encodeToString(("Bearer " + token).getBytes(StandardCharsets.UTF_8));
//		Cookie cookie = new Cookie("Authorization", encodedToken);
//	    cookie.setHttpOnly(true);
//	    cookie.setSecure(true); // HTTPS로만 전달되도록 설정
//	    cookie.setPath("/"); // 애플리케이션의 모든 경로에서 유효하도록 설정
//	    cookie.setMaxAge(3600); // 쿠키의 만료 시간 설정 (초 단위, 3600초는 1시간)
//
//	    response.addCookie(cookie);
//		
//	    SecurityContextHolder.getContext().setAuthentication(authentication);

	}

	// 로그인 실패시 처리
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		log.info(" failure ");
		response.setStatus(401);
	}
	
	private static class UserCredentials {
		private String email;
		private String password;

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
	}
	
	// application/x-www-form-urlencoded 데이터를 MemberSigninRequest 객체로 변환하는 메서드
    private MemberSigninRequest parseFormUrlencodedBody(String body) {
        MemberSigninRequest loginRequest = new MemberSigninRequest();
        String[] params = body.split("&");
        for (String param : params) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2) {
                String key = keyValue[0];
                String value = keyValue[1];
                if ("email".equals(key)) {
                    loginRequest.setEmail(value);
                } else if ("password".equals(key)) {
                    loginRequest.setPassword(value);
                }
            }
        }
        return loginRequest;
    }
}
