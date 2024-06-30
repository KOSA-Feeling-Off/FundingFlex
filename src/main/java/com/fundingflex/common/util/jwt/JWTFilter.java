package com.fundingflex.common.util.jwt;

import java.io.IOException;

import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fundingflex.member.domain.dto.CustomUserDetails;
import com.fundingflex.member.domain.entity.Members;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Order(1)
@RequiredArgsConstructor
@Log4j2
public class JWTFilter extends OncePerRequestFilter {
	
	private final JWTUtil jwtUtil;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authorization = request.getHeader("Authorization");
		
		log.info("@@@@@@@@@@@@@@@@@@@@@@Authorization header value: {}", authorization); // 추가된 로그

		if (authorization == null || !authorization.startsWith("Bearer ")) {
			log.info("Authorization header is missing or invalid");
			filterChain.doFilter(request, response);
			return;
		}

		String token = authorization.substring(7); // "Bearer " 이후의 토큰 부분만 추출
		log.info("Extracted Token: {}", token);

		try {
			if (jwtUtil.isExpired(token)) {
				log.info("Token is expired");
				filterChain.doFilter(request, response);
				return;
			}

			String email = jwtUtil.getEmail(token);
			String role = jwtUtil.getRole(token);

			Members members = new Members();
			members.setEmail(email);
			members.setRole(role);

			CustomUserDetails customUserDetails = new CustomUserDetails(members);
			Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null,
					customUserDetails.getAuthorities());

			SecurityContextHolder.getContext().setAuthentication(authToken);
            log.info("Authentication set in SecurityContextHolder for user: {}", email);
		} catch (Exception e) {
			SecurityContextHolder.clearContext();
			log.error("Authentication error: ", e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
		filterChain.doFilter(request, response);
	}
}