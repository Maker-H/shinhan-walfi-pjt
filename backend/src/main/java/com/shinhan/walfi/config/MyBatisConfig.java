package com.shinhan.walfi.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@MapperScan(basePackages = "com.shinhan.walfi.mapper")
public class MyBatisConfig {
}
