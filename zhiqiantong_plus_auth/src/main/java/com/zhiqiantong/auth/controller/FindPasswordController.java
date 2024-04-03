package com.zhiqiantong.auth.controller;

import com.zhiqiantong.ucenter.model.dto.FindPasswordDto;
import com.zhiqiantong.ucenter.service.FindPasswordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class FindPasswordController {

    @Autowired
    FindPasswordService findPasswordService;

    @PostMapping("/findpassword")
    public void findPassword(@RequestBody FindPasswordDto findPasswordDto) {

        findPasswordService.findPassword(findPasswordDto);

    }

}
