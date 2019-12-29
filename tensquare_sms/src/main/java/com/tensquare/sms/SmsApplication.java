package com.tensquare.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @program: tensquare_parent
 * @description:
 * @author: 好人
 * @date: 2019-12-26 10:53
 **/
@SpringBootApplication
@EnableEurekaClient
public class SmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmsApplication.class,args);
    }
}
