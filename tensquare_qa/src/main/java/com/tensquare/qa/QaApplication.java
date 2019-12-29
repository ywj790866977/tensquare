package com.tensquare.qa;

import io.jsonwebtoken.Jwt;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import util.IdWorker;
import util.JwtUtil;

/**
 * @program: tensquare_parent
 * @description:
 * @author: 好人
 * @date: 2019-12-22 19:00
 **/
@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients
public class QaApplication {
    public static void main(String[] args) {
        SpringApplication.run(QaApplication.class,args);
    }

    @Bean
    public IdWorker idWorker(){
        return new IdWorker(1,1);
    }

    @Bean
    public JwtUtil jwtUtil(){
        return new JwtUtil();
    }
}
