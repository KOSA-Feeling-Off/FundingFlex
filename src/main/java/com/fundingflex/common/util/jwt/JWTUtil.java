package com.fundingflex.common.util.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JWTUtil {
	
	private final SecretKey secretKey; // JWT 서명에 사용될 비밀 키
	UserDetailsService userDetailsService;

	public JWTUtil(@Value("${jwt.secret}") String secret, UserDetailsService userDetailsService) {
//		// 비밀 키를 바이트 배열로 변환하고 HMAC SHA-256 알고리즘을 사용하여 SecretKeySpec 객체 생성
//		this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
//				Jwts.SIG.HS256.key().build().getAlgorithm());
		this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.userDetailsService = userDetailsService;

	}

	public SecretKey getSecretKey() {
		return this.secretKey;
	}

//	public Authentication getAuthentication(String token) {
//		Claims claims = getClaims;
//	}

	// JWT에서 사용자 이메일을 추출합니다.
	public String getEmail(String token) {
		return Jwts.parser() // JWT 파서를 생성
				.verifyWith(secretKey) // 비밀 키로 서명을 검증
				.build() // 파서 빌드
				.parseSignedClaims(token) // 서명된 JWT를 파싱
				.getPayload() // 페이로드를 가져옴
				.get("email", String.class); // 페이로드에서 "username" 클레임을 문자열로 가져옴
	}

	// JWT에서 사용자 역할을 추출합니다.
	public String getRole(String token) {
		return Jwts.parser() // JWT 파서를 생성
				.verifyWith(secretKey) // 비밀 키로 서명을 검증
				.build() // 파서 빌드
				.parseSignedClaims(token) // 서명된 JWT를 파싱
				.getPayload() // 페이로드를 가져옴
				.get("role", String.class); // 페이로드에서 "role" 클레임을 문자열로 가져옴
	}

	// JWT가 만료되었는지 확인합니다.
	public Boolean isExpired(String token) {
		return Jwts.parser() // JWT 파서를 생성
				.verifyWith(secretKey) // 비밀 키로 서명을 검증
				.build() // 파서 빌드
				.parseSignedClaims(token) // 서명된 JWT를 파싱
				.getPayload() // 페이로드를 가져옴
				.getExpiration() // 만료 날짜를 가져옴
				.before(new Date()); // 현재 날짜와 비교하여 만료 여부를 반환
	}

	// 새로운 JWT를 생성합니다.
	public String createJwt(String email, String role, Long expiredMs) {
	    long now = System.currentTimeMillis();

	    // JWT 생성
	    return Jwts.builder()
	            .claim("email", email) // "username" 클레임에 이메일 추가
	            .claim("role", role) // "role" 클레임에 사용자 역할 추가
	            .issuedAt(new Date(now)) // 발급 시간
	            .expiration(new Date(now + expiredMs)) // 만료 시간
	            .compact(); // 직렬화하여 문자열로 반환
	}
	
	// request의 header에서 토큰값 가져옴
	public String resolveToken(HttpServletRequest request) {
	    String bearerToken = request.getHeader("Authorization");
	    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
	        return bearerToken.substring(7);
	    }
	    return null;
	} 
	
	//jwt 토큰에서 인증 정보 조회
	public Authentication getAuthentication(String token) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(this.getEmail(token));
		log.info(userDetails.getUsername());
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}
}