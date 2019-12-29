package com.tensquare.recruit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import util.IdWorker;


/**
 * @program: tensquare_parent
 * @description:
 * @author: 好人
 * @date: 2019-12-20 17:26
 **/
@SpringBootApplication
@EnableEurekaClient
public class RecruitApplication {
    public static void main(String[] args) {
        SpringApplication.run(RecruitApplication.class,args);
    }

    @Bean
    public IdWorker idWorker(){
        return new IdWorker(1,1);
    }
}
