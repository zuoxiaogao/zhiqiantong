package com.zhiqiantong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages={"com.zhiqiantong.*.feignclient"})
@SpringBootApplication(scanBasePackages = {"com.zhiqiantong.base","com.zhiqiantong.content","com.zhiqiantong.messagesdk","com.zhiqiantong.learning"})
public class LearningApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearningApplication.class, args);
    }

}
