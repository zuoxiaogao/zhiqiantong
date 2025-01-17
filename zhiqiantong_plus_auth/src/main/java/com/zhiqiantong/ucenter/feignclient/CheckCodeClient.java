package com.zhiqiantong.ucenter.feignclient;

import com.zhiqiantong.ucenter.fallbackfactory.CheckCodeClientFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Mr.M
 * @version 1.0
 * @description 验证码服务远程接口
 * @date 2022/9/20 20:29
 */
@FeignClient(value = "checkcode", fallbackFactory = CheckCodeClientFactory.class)
@RequestMapping("/checkcode")
public interface CheckCodeClient {

    @PostMapping(value = "/verify")
    Boolean verify(@RequestParam("key") String key, @RequestParam("code") String code);

}
