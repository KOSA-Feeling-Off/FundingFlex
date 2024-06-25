package com.fundingflex.util;

import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // 현재 사용자 정보 반환
        // Spring Security를 사용하는 경우 SecurityContextHolder에서 사용자 정보를 가져와야함
        return Optional.of("system"); // 임시로 "system" 사용자로 설정
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return Optional.empty();
//        }
//
//        Object principal = authentication.getPrincipal();
//        if (principal instanceof CustomUserDetails) {
//            return Optional.of(((CustomUserDetails) principal).getNickname());
//
//        } else {
//            return Optional.of(principal.toString());
//        }
    }

    @Bean
    AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }
}