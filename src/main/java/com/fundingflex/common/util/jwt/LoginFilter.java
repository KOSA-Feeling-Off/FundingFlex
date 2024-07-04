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
//	    if ("GET".equalsIgnoreCase(request.getMethod())) {
//	        // GET 요청 무시 또는 적절한 응답 반환
//	        try {
//	            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "GET method is not supported");
//	            return null;
//	        } catch (IOException e) {
//	            throw new RuntimeException(e);
//	        }
//	    }

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
	        	request.getSession().setAttribute("error", "이메일 혹은 패스워드를 입력하세요.");
	            log.error("Email or Password is empty");
	            response.sendRedirect("/api/login");
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
		    response.sendRedirect("/api/home");
	}

	// 로그인 실패시 처리
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		log.info("Login failure: " + failed.getMessage());
	    
	    // 실패 원인을 세션에 저장
	    request.getSession().setAttribute("error", "이메일 또는 패스워드가 정확하지 않습니다.");
		log.info(" failure ");
		response.sendRedirect("/api/login");
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
