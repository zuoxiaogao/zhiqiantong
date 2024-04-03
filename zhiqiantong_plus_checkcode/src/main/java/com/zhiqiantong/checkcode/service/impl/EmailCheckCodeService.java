package com.zhiqiantong.checkcode.service.impl;

import com.zhiqiantong.base.execption.ZhiQianTongPlusException;
import com.zhiqiantong.checkcode.model.CheckCodeParamsDto;
import com.zhiqiantong.checkcode.model.CheckCodeResultDto;
import com.zhiqiantong.checkcode.service.AbstractCheckCodeService;
import com.zhiqiantong.checkcode.service.CheckCodeService;
import com.zhiqiantong.checkcode.utils.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;

@Service("EmailCheckCodeService")
public class EmailCheckCodeService extends AbstractCheckCodeService implements CheckCodeService {

    @Autowired
    MailUtil mailUtil;

    @Resource(name="SexNumberCheckCodeGenerator")
    @Override
    public void setCheckCodeGenerator(CheckCodeGenerator checkCodeGenerator) {
        this.checkCodeGenerator = checkCodeGenerator;
    }

    @Resource(name="UUIDKeyGenerator")
    @Override
    public void setKeyGenerator(KeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }


    @Resource(name="RedisCheckCodeStore")
    @Override
    public void setCheckCodeStore(CheckCodeStore checkCodeStore) {
        this.checkCodeStore = checkCodeStore;
    }


    @Override
    public CheckCodeResultDto generate(CheckCodeParamsDto checkCodeParamsDto) {
        GenerateResult generate = generate(checkCodeParamsDto, 6, "checkcode:", 60);
        String key = generate.getKey();
        String code = generate.getCode();
        try {
            mailUtil.sendTestMail(checkCodeParamsDto.getParam1(),code);
        } catch (MessagingException e) {
            ZhiQianTongPlusException.cast("发送验证码失败，请重试！");
        }
        CheckCodeResultDto checkCodeResultDto = new CheckCodeResultDto();
        checkCodeResultDto.setKey(key);
        return checkCodeResultDto;
    }
}
