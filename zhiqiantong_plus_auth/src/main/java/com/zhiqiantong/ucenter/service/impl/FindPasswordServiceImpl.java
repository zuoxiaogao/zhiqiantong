package com.zhiqiantong.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhiqiantong.base.execption.ZhiQianTongPlusException;
import com.zhiqiantong.ucenter.feignclient.CheckCodeClient;
import com.zhiqiantong.ucenter.mapper.ZqtUserMapper;
import com.zhiqiantong.ucenter.model.dto.FindPasswordDto;
import com.zhiqiantong.ucenter.model.po.ZqtUser;
import com.zhiqiantong.ucenter.service.FindPasswordService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class FindPasswordServiceImpl implements FindPasswordService {

    @Autowired
    CheckCodeClient checkCodeClient;

    @Autowired
    ZqtUserMapper zqtUserMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void findPassword(FindPasswordDto findPasswordDto) {
        String phone = findPasswordDto.getCellphone();
        String email = findPasswordDto.getEmail();
        if (StringUtils.isBlank(phone) && StringUtils.isBlank(email)) {
            ZhiQianTongPlusException.cast("手机号和密码都为空。请重试！");
        }
        String checkcodekey = findPasswordDto.getCheckcodekey();
        String checkcode = findPasswordDto.getCheckcode();
        if (StringUtils.isBlank(checkcodekey) || StringUtils.isBlank(checkcode)) {
            ZhiQianTongPlusException.cast("验证码有误，请重试！");
        }
        Boolean verify = checkCodeClient.verify(checkcodekey, checkcode);
        if (!verify) {
            ZhiQianTongPlusException.cast("验证码有误，请重试！");
        }
        String password = findPasswordDto.getPassword();
        String confirmpwd = findPasswordDto.getConfirmpwd();
        if (StringUtils.isBlank(password) || StringUtils.isBlank(confirmpwd)) {
            ZhiQianTongPlusException.cast("密码或者确认密码为空，请重试！");
        }
        if (!password.equals(confirmpwd)) {
            ZhiQianTongPlusException.cast("两次输入的密码不一致，请重试！");
        }
        LambdaQueryWrapper<ZqtUser> lqw = new LambdaQueryWrapper<>();
        lqw.eq(!StringUtils.isBlank(phone),ZqtUser::getCellphone,phone);
        lqw.eq(!StringUtils.isBlank(email),ZqtUser::getEmail,email);
        ZqtUser zqtUser = zqtUserMapper.selectOne(lqw);
        if (zqtUser == null) {
            ZhiQianTongPlusException.cast("账号不存在，请先注册！");
        }
        String encode = passwordEncoder.encode(password);
        zqtUser.setPassword(encode);
        int i = zqtUserMapper.updateById(zqtUser);
        if (i <= 0) {
            ZhiQianTongPlusException.cast("找回密码失败，请稍后重试！");
        }
    }
}
