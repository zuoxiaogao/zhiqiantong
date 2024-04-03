package com.zhiqiantong.auth.controller;

import com.zhiqiantong.ucenter.model.dto.FindPasswordDto;
import com.zhiqiantong.ucenter.model.dto.RegisterParamDto;
import com.zhiqiantong.ucenter.service.RegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class RegisterController {

    @Autowired
    RegisterService registerService;

    @PostMapping("/register")
    public void register(@RequestBody RegisterParamDto registerParamDto) {

        registerService.register(registerParamDto);

    }

}
