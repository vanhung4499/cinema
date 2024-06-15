package com.hnv99.framework.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
// Configure the package to scan for mappers
@MapperScan("com.hnv99.**.mapper")
public class ApplicationConfiguration {
}
