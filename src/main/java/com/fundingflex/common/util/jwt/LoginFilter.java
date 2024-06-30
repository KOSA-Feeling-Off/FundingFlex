package com.fundingflex.common.util.jwt;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fundingflex.member.domain.dto.CustomUserDetails;
import com.fundingflex.member.domain.dto.MemberSigninRequest;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
	        ObjectMapper objectMapper = new ObjectMapper();
	        if (request.getInputStream().available() == 0) {
	            log.error("Request body is empty");
	            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Request body is empty");
	            return null;
	        }

	        // 요청 본문을 문자열로 읽어 로그에 출력
	        String requestBody = new String(request.getInputStream().readAllBytes());
	        log.info("Request Body: " + requestBody);

	        MemberSigninRequest loginRequest = objectMapper.readValue(requestBody, MemberSigninRequest.class);

	        if (loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
	            log.error("Email or Password is empty");
	            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email or Password is empty");
	            return null;
	        }

	        log.info("Attempting authentication for user: {}", loginRequest.getEmail());

	        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
	                loginRequest.getEmail(), loginRequest.getPassword());

	        return authenticationManager.authenticate(authToken);

	    } catch (IOException e) {
	        throw new RuntimeException(e);
	    }
	}

	// 로그인 성공시 JWT를 발급
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,	
			Authentication authentication) throws IOException, ServletException {
		log.info("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ success / Authentication successful for user: " + authentication.getName());

		if (!(authentication.getPrincipal() instanceof CustomUserDetails)) {
	        throw new UsernameNotFoundException("User details not found");
	    }
		
		// CustomUserDetails 객체를 가져옵니다. 이는 인증된 사용자 정보를 포함합니다.
		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
		// 사용자 이름을 CustomUserDetails 객체에서 가져옵니다.
		String email = customUserDetails.getUsername(); // email
		// 인증된 사용자의 권한(role)을 가져옵니다.
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
		GrantedAuthority auth = iterator.next();
		// 첫 번째 권한의 이름(역할)을 가져옵니다.
		String role = auth.getAuthority();
		// JWT 토큰을 생성합니다. 토큰은 사용자 이름과 역할, 유효 기간을 포함합니다.
		String token = jwtUtil.createJwt(email, role, 60 * 60 * 10L); // 10시간
		// 생성된 JWT 토큰을 HTTP 응답 헤더에 추가합니다.
		response.addHeader("Authorization", "Bearer " + token);
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
