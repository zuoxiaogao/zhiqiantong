package com.zhiqiantong.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhiqiantong.ucenter.feignclient.CheckCodeClient;
import com.zhiqiantong.ucenter.mapper.ZqtUserMapper;
import com.zhiqiantong.ucenter.model.dto.AuthParamsDto;
import com.zhiqiantong.ucenter.model.dto.ZqtUserExt;
import com.zhiqiantong.ucenter.model.po.ZqtUser;
import com.zhiqiantong.ucenter.service.AuthService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Mr.M
 * @version 1.0
 * @description 账号密码认证
 * @date 2022/9/29 12:12
 */
@Service("password_authservice")
public class PasswordAuthServiceImpl implements AuthService {

    @Autowired
    ZqtUserMapper zqtUserMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CheckCodeClient checkCodeClient;

    @Override
    public ZqtUserExt execute(AuthParamsDto authParamsDto) {
        //校验验证码
        String checkcode = authParamsDto.getCheckcode();
        String checkcodekey = authParamsDto.getCheckcodekey();

        if(StringUtils.isBlank(checkcodekey) || StringUtils.isBlank(checkcode)){
            throw new RuntimeException("验证码为空");
        }

        Boolean verify = checkCodeClient.verify(checkcodekey, checkcode);

        if(!verify){
            throw new RuntimeException("验证码输入错误");
        }

        //账号
        String username = authParamsDto.getUsername();
        LambdaQueryWrapper<ZqtUser> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ZqtUser::getUsername, username).or().eq(ZqtUser::getCellphone,username).or().eq(ZqtUser::getEmail,username);
        ZqtUser user = zqtUserMapper.selectOne(lqw);
        if (user == null) {
            //返回空表示用户不存在
            throw new RuntimeException("账号不存在");
        }
        ZqtUserExt zqtUserExt = new ZqtUserExt();
        BeanUtils.copyProperties(user, zqtUserExt);
        //校验密码
        //取出数据库存储的正确密码
        String passwordDb = user.getPassword();
        String passwordForm = authParamsDto.getPassword();
        boolean matches = passwordEncoder.matches(passwordForm, passwordDb);
        if (!matches) {
            throw new RuntimeException("账号或密码错误");
        }
        return zqtUserExt;
    }
}
