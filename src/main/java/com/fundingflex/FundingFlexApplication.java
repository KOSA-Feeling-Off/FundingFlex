package com.fundingflex;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication(exclude = SecurityAutoConfiguration.class) //스프링 시큐리티 비활성화
@SpringBootApplication
@MapperScan("com.fundingflex.mybatis.mapper")
public class FundingFlexApplication {

	public static void main(String[] args) {
		SpringApplication.run(FundingFlexApplication.class, args);
	}

}
