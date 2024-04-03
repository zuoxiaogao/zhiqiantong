package com.zhiqiantong.checkcode.service.impl;

import com.zhiqiantong.checkcode.model.CheckCodeParamsDto;
import com.zhiqiantong.checkcode.model.CheckCodeResultDto;
import com.zhiqiantong.checkcode.service.AbstractCheckCodeService;
import com.zhiqiantong.checkcode.service.CheckCodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("PhoneCheckCodeService")
public class PhoneCheckCodeService extends AbstractCheckCodeService implements CheckCodeService {

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
        CheckCodeResultDto checkCodeResultDto = new CheckCodeResultDto();
        checkCodeResultDto.setKey(key);
        return checkCodeResultDto;
    }
}
