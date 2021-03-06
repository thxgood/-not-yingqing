package com.example.serch;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
@EnableEurekaClient
@SpringBootApplication
@MapperScan("com.example.serch.mapper")
public class SerchApplication {
    public static void main(String[] args) {
        SpringApplication.run(SerchApplication.class, args);
    }

}
