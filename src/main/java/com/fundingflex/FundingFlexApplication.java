package com.fundingflex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class) //스프링 시큐리티 비활성화
//@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class FundingFlexApplication {

   public static void main(String[] args) {
      SpringApplication.run(FundingFlexApplication.class, args);
   }
}