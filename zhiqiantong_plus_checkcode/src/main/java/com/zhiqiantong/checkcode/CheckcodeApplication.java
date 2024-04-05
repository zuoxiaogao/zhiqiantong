package com.zhiqiantong.checkcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.zhiqiantong.base","com.zhiqiantong.checkcode"})
public class CheckcodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CheckcodeApplication.class, args);
    }

}
