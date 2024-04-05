package com.zhiqiantong;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableSwagger2Doc
@EnableTransactionManagement
@SpringBootApplication(scanBasePackages = {"com.zhiqiantong.base","com.zhiqiantong.content","com.zhiqiantong.messagesdk"})
@EnableFeignClients(basePackages={"com.zhiqiantong.content.feignclient"})
public class ContentApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContentApplication.class, args);
    }

}

