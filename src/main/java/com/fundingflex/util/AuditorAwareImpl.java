package com.fundingflex.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@Configuration
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // 현재 사용자 정보 반환
        // Spring Security를 사용하는 경우 SecurityContextHolder에서 사용자 정보를 가져와야함
        return Optional.of("system"); // 임시로 "system" 사용자로 설정
    }

    @Bean
    AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }
}