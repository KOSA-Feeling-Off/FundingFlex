package com.fundingflex.member.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fundingflex.common.util.jwt.JWTFilter;
import com.fundingflex.common.util.jwt.JWTUtil;
import com.fundingflex.common.util.jwt.LoginFilter;
import com.fundingflex.member.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration // Spring Bean Configuration 클래스 선언.
@EnableWebSecurity // Spring Security를 활성화합니다.
@RequiredArgsConstructor
public class SecurityConfig {
	//AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
	private final AuthenticationConfiguration authenticationConfiguration;
	private final JWTUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;


	/**
	 * BCryptPasswordEncoder는 비밀번호를 해싱하는 데 사용됩니다. BCrypt 해시 함수는 각 해시마다 고유한 솔트를 생성하여
	 * 보안을 강화합니다.
	 */
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	//AuthenticationManager Bean 등록
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
	
	@Bean
    public CustomAuthenticationEntryPoint customAuthenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();// 추가코드 2
		    http.csrf(csrf -> csrf.disable())
		    	.formLogin(form -> form
		    			.loginPage("/api/login")
		    			.loginProcessingUrl("/api/signin")
		    			.successHandler(new CustomAuthenticationSuccessHandler())
		    			.permitAll())
		        .httpBasic(basic -> basic.disable())
		        .authorizeHttpRequests(auth -> auth
		            .requestMatchers("/css/**", "/fonts/**", "/img/**", "/js/**", "/images/**").permitAll()  // 정적 리소스 접근 허용
		            .requestMatchers("/", "/api/home", "/api/login", "/api/signin", "/api/signup", "/api/fundings/list/**"
		            		, "/qa/fqa", "/api/categories/**", "/api/fundings/*/details/*", "/sse/**").permitAll()
//		            .anyRequest().permitAll())
		            .anyRequest().authenticated())  // 그 외의 모든 요청은 인증을 요구
		        .addFilterBefore(new LoginFilter(authenticationManager, jwtUtil), UsernamePasswordAuthenticationFilter.class)
		        .addFilterAfter(new JWTFilter(jwtUtil, customUserDetailsService), LoginFilter.class)
		        .sessionManagement(session -> session
		            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		        .logout((logout) -> logout
		                .logoutRequestMatcher(new AntPathRequestMatcher("/api/logout"))
		                .logoutSuccessHandler(new CustomLogoutSuccessHandler())  // 로그아웃 성공 핸들러 지정
		                .invalidateHttpSession(true));
		    return http.build();
	}
}