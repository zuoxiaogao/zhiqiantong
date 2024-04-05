package com.zhiqiantong.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhiqiantong.base.execption.ZhiQianTongPlusException;
import com.zhiqiantong.base.utils.StringUtil;
import com.zhiqiantong.ucenter.feignclient.CheckCodeClient;
import com.zhiqiantong.ucenter.mapper.ZqtRoleMapper;
import com.zhiqiantong.ucenter.mapper.ZqtUserMapper;
import com.zhiqiantong.ucenter.mapper.ZqtUserRoleMapper;
import com.zhiqiantong.ucenter.model.dto.RegisterParamDto;
import com.zhiqiantong.ucenter.model.po.ZqtRole;
import com.zhiqiantong.ucenter.model.po.ZqtUser;
import com.zhiqiantong.ucenter.model.po.ZqtUserRole;
import com.zhiqiantong.ucenter.service.RegisterService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    CheckCodeClient checkCodeClient;
    @Autowired
    ZqtUserMapper zqtUserMapper;
    @Autowired
    ZqtUserRoleMapper zqtUserRoleMapper;
    @Autowired
    ZqtRoleMapper zqtRoleMapper;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void register(RegisterParamDto registerParamDto) {
        String phone = registerParamDto.getCellphone();
        if (StringUtil.isBlank(phone)) {
            ZhiQianTongPlusException.cast("手机号为空，请重试！");
        }
        String checkcodekey = registerParamDto.getCheckcodekey();
        String checkcode = registerParamDto.getCheckcode();
        if (StringUtil.isBlank(checkcode) || StringUtil.isBlank(checkcodekey)) {
            ZhiQianTongPlusException.cast("验证码错误，请重试！");
        }
        Boolean verify = checkCodeClient.verify(checkcodekey, checkcode);
        if (!verify) {
            ZhiQianTongPlusException.cast("验证码错误，请重试！");
        }
        String password = registerParamDto.getPassword();
        String confirmpwd = registerParamDto.getConfirmpwd();
        if (StringUtil.isBlank(password) || StringUtil.isBlank(confirmpwd)) {
            ZhiQianTongPlusException.cast("两次输入的密码不一致，请重试！");
        }
        if (!password.equals(confirmpwd)) {
            ZhiQianTongPlusException.cast("两次输入的密码不一致，请重试！");
        }
        LambdaQueryWrapper<ZqtUser> lqw = new LambdaQueryWrapper<>();
        String username = registerParamDto.getUsername();
        if (StringUtil.isBlank(username)) {
            ZhiQianTongPlusException.cast("用户名不能为空，请重试！");
        }
        lqw.eq(ZqtUser::getUsername,username);
        ZqtUser zqtUser = zqtUserMapper.selectOne(lqw);
        if (zqtUser != null) {
            ZhiQianTongPlusException.cast("该用户名已经被使用，请更换！");
        }
        lqw.clear();
        lqw.eq(ZqtUser::getCellphone,phone);
        zqtUser = zqtUserMapper.selectOne(lqw);
        if (zqtUser != null) {
            ZhiQianTongPlusException.cast("该手机号已经被注册，请去登陆！");
        }
        String email = registerParamDto.getEmail();
        if (email != null) {
            lqw.clear();
            lqw.eq(ZqtUser::getEmail,email);
            zqtUser = zqtUserMapper.selectOne(lqw);
            if (zqtUser != null) {
                ZhiQianTongPlusException.cast("该邮箱已经被使用，请更换！");
            }
        }
        zqtUser = new ZqtUser();
        BeanUtils.copyProperties(registerParamDto,zqtUser);
        zqtUser.setUtype("101001");
        zqtUser.setStatus("1");
        zqtUser.setCreateTime(LocalDateTime.now());
        zqtUser.setPassword(passwordEncoder.encode(password));
        int insert = zqtUserMapper.insert(zqtUser);
        if (insert <= 0) {
            ZhiQianTongPlusException.cast("注册失败，请稍后重试！");
        }
        ZqtUserRole zqtUserRole = new ZqtUserRole();
        zqtUserRole.setUserId(zqtUser.getId());
        zqtUserRole.setCreateTime(LocalDateTime.now());
        LambdaQueryWrapper<ZqtRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ZqtRole::getRoleName,"学生");
        ZqtRole zqtRole = zqtRoleMapper.selectOne(queryWrapper);
        if (zqtRole == null || zqtRole.getId() == null) {
            ZhiQianTongPlusException.cast("注册失败，请稍后重试！");
        }
        zqtUserRole.setRoleId(zqtRole.getId());
        int i = zqtUserRoleMapper.insert(zqtUserRole);
        if (i <= 0) {
            ZhiQianTongPlusException.cast("注册失败，请稍后重试！");
        }
    }

}
