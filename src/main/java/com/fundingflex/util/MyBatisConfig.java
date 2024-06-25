package com.fundingflex.util;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Component;

@Component
@MapperScan("com.fundingflex.mybatismapper.mapper")
public class MyBatisConfig {

	
}
