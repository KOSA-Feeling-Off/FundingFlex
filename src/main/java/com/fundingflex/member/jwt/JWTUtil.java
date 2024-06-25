package com.fundingflex.member.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;

@Component
public class JWTUtil {
	private SecretKey secretKey; // JWT 서명에 사용될 비밀 키
	// 생성자에서 비밀 키를 초기화합니다.

	public JWTUtil(@Value("${jwt.secret}") String secret) {
		// 비밀 키를 바이트 배열로 변환하고 HMAC SHA-256 알고리즘을 사용하여 SecretKeySpec 객체 생성
		this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
				Jwts.SIG.HS256.key().build().getAlgorithm());
	}

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
	public String createJwt(String username, String role, Long expiredMs) {
		return Jwts.builder() // JWT 빌더를 생성
				.claim("username", username) // "username" 클레임을 추가
				.claim("role", role) // "role" 클레임을 추가
				.issuedAt(new Date(System.currentTimeMillis())) // JWT 발급 시간을 현재 시간으로 설정
				.expiration(new Date(System.currentTimeMillis() + expiredMs)) // JWT 만료 시간을 설정
				.signWith(secretKey) // 비밀 키로 서명
				.compact(); // JWT를 직렬화하여 압축된 문자열로 반환
	}
}