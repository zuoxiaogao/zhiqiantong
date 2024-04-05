package com.zhiqiantong.checkcode.service.impl;

import com.zhiqiantong.checkcode.service.CheckCodeService;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component("SexNumberCheckCodeGenerator")
public class SexNumberCheckCodeGenerator implements CheckCodeService.CheckCodeGenerator {
    @Override
    public String generate(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

}
