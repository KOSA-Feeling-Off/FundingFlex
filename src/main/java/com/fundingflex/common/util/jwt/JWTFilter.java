package com.fundingflex.common.util.jwt;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fundingflex.member.service.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Order(1)
@RequiredArgsConstructor
@Log4j2
public class JWTFilter extends OncePerRequestFilter {
	
	private final JWTUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        String jwt = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("Authorization".equals(cookie.getName())) {
                	String encodedToken = cookie.getValue();
                    jwt = new String(Base64.getUrlDecoder().decode(encodedToken), StandardCharsets.UTF_8);
                    break;
                }
            }
        }

        log.info("JWT Token: {}", jwt);

        if (StringUtils.hasText(jwt) && jwtUtil.validateToken(jwt)) {
            Authentication authentication = jwtUtil.getAuthentication(jwt);
            log.info("Authentication: {}", authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//        String authorization = request.getHeader("Authorization");
//
//        if (authorization == null || !authorization.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String token = authorization.substring(7);
//        if (jwtUtil.isExpired(token)) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String email = jwtUtil.getEmail(token);
//        CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(email);
//
//        if (userDetails != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                    userDetails, null, userDetails.getAuthorities());
//            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//            SecurityContextHolder.getContext().setAuthentication(authToken);
//        }
//
//        filterChain.doFilter(request, response);
//    }
	
//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//			throws ServletException, IOException {
//		String authorization = request.getHeader("Authorization");
//		
//		log.info("@@@@@@@@@@@@@@@@@@@@@@Authorization header value: {}", authorization); // 추가된 로그
//
//		if (authorization == null || !authorization.startsWith("Bearer ")) {
//			log.info("Authorization header is missing or invalid");
//			filterChain.doFilter(request, response);
//			return;
//		}
//
//		String token = authorization.substring(7); // "Bearer " 이후의 토큰 부분만 추출
//		log.info("Extracted Token: {}", token);
//
//		try {
//			if (jwtUtil.isExpired(token)) {
//				log.info("Token is expired");
//				filterChain.doFilter(request, response);
//				return;
//			}
//
//			String email = jwtUtil.getEmail(token);
//			String role = jwtUtil.getRole(token);
//
//			Members members = new Members();
//			members.setEmail(email);
//			members.setRole(role);
//
//			CustomUserDetails customUserDetails = new CustomUserDetails(members);
//			Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null,
//					customUserDetails.getAuthorities());
//
//			SecurityContextHolder.getContext().setAuthentication(authToken);
//            log.info("Authentication set in SecurityContextHolder for user: {}", email);
//		} catch (Exception e) {
//			SecurityContextHolder.clearContext();
//			log.error("Authentication error: ", e);
//			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//			return;
//		}
//		filterChain.doFilter(request, response);
//	}
}