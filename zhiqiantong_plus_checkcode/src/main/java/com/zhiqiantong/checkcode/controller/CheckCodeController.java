package com.zhiqiantong.checkcode.controller;

import com.zhiqiantong.checkcode.model.CheckCodeParamsDto;
import com.zhiqiantong.checkcode.model.CheckCodeResultDto;
import com.zhiqiantong.checkcode.service.CheckCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Mr.M
 * @version 1.0
 * @description 验证码服务接口
 * @date 2022/9/29 18:39
 */
@Api(value = "验证码服务接口")
@RestController
public class CheckCodeController {

    @Resource(name = "PicCheckCodeService")
    private CheckCodeService picCheckCodeService;

    @Resource(name = "PhoneCheckCodeService")
    private CheckCodeService phoneCheckCodeService;

    @Resource(name = "EmailCheckCodeService")
    private CheckCodeService emailCheckCodeService;


    @ApiOperation(value="生成验证信息", notes="生成验证信息")
    @PostMapping(value = "/pic")
    public CheckCodeResultDto generatePicCheckCode(CheckCodeParamsDto checkCodeParamsDto){
        checkCodeParamsDto.setCheckCodeType("pic");
        return picCheckCodeService.generate(checkCodeParamsDto);
    }

    @ApiOperation(value="校验", notes="校验")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "业务名称", required = true, dataType = "String", paramType="query"),
            @ApiImplicitParam(name = "key", value = "验证key", required = true, dataType = "String", paramType="query"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = "String", paramType="query")
    })
    @PostMapping(value = "/verify")
    public Boolean verify(String key, String code){
        return picCheckCodeService.verify(key,code);
    }

    @ApiOperation(value="生成手机号验证信息", notes="生成手机号验证信息")
    @PostMapping(value = "/phone")
    public CheckCodeResultDto generatePhoneCheckCode(CheckCodeParamsDto checkCodeParamsDto){
        checkCodeParamsDto.setCheckCodeType("sms");
        return phoneCheckCodeService.generate(checkCodeParamsDto);
    }

    @ApiOperation(value="生成邮箱验证信息", notes="生成邮箱验证信息")
    @PostMapping(value = "/email")
    public CheckCodeResultDto generateEmailCheckCode(CheckCodeParamsDto checkCodeParamsDto){
        checkCodeParamsDto.setCheckCodeType("email");
        return emailCheckCodeService.generate(checkCodeParamsDto);
    }
}
