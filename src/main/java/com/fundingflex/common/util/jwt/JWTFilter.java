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
}