package com.zhiqiantong.auth.controller;

import com.zhiqiantong.ucenter.mapper.ZqtUserMapper;
import com.zhiqiantong.ucenter.model.dto.FindPasswordDto;
import com.zhiqiantong.ucenter.model.po.ZqtUser;
import com.zhiqiantong.ucenter.service.FindPasswordService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Mr.M
 * @version 1.0
 * @description 测试controller
 * @date 2022/9/27 17:25
 */
@Slf4j
@RestController
public class LoginController {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    ZqtUserMapper userMapper;


    @RequestMapping("/login-success")
    public String loginSuccess() {

        return "登录成功";
    }

    @RequestMapping("/user/{id}")
    public ZqtUser getUser(@PathVariable("id") String id) {
        return userMapper.selectById(id);
    }

    @PreAuthorize("hasAuthority('p1')")
    @RequestMapping("/r/r1")
    public String r1() {
        return "访问r1资源";
    }

    @PreAuthorize("hasAuthority('p2')")
    @RequestMapping("/r/r2")
    public String r2() {
        return "访问r2资源";
    }



}
