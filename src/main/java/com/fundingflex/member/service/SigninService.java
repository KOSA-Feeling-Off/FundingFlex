package com.fundingflex.member.service;

import com.fundingflex.member.jwt.JWTUtil;
import com.fundingflex.member.vaildation.MemberLoginForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SigninService {

	private final AuthenticationManager authenticationManager;
	private final JWTUtil jwtUtil;
	private final CustomUserDetailsService userDetailsService;

	public String authenticateAndGenerateToken(MemberLoginForm loginForm) throws AuthenticationException {
//		// 사용자 인증을 시도합니다.
//		Authentication authentication = authenticationManager
//				.authenticate(new UsernamePasswordAuthenticationToken(loginForm.getEmail(), loginForm.getPassword()));
//
//		// 인증된 사용자 정보를 가져옵니다.
//		UserDetails userDetails = userDetailsService.loadUserByUsername(loginForm.getEmail());
//
//		// JWT 토큰을 생성합니다.
//		return jwtUtil.createJwt(userDetails.getUsername(),
//				userDetails.getAuthorities().iterator().next().getAuthority(), 3600000L);
//	}

		try {
			// 사용자 인증
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginForm.getEmail(), loginForm.getPassword()));
			
			// 인증된 사용자 이름과 권한 가져오기
			String email = authentication.getName();
			String role = authentication.getAuthorities().iterator().next().getAuthority();
			
			if (!"USER".equals(role)) {
                log.error("Invalid role: {}", role);
                throw new AuthenticationException("Invalid role") {};
            }

			/// JWT 토큰 생성
	        String token = jwtUtil.createJwt(email, role, 60 * 60 * 1000L); // 1시간 유효 기간
	        log.info("Generated JWT Token: {}", token);
	        return token;
		} catch (AuthenticationException e) {
			log.error("Authentication failed", e);
	        throw new RuntimeException("Invalid login credentials");
		}
	}
}