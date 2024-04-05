package com.zhiqiantong.ucenter.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhiqiantong.ucenter.mapper.ZqtMenuMapper;
import com.zhiqiantong.ucenter.mapper.ZqtUserMapper;
import com.zhiqiantong.ucenter.model.dto.AuthParamsDto;
import com.zhiqiantong.ucenter.model.dto.ZqtUserExt;
import com.zhiqiantong.ucenter.model.po.ZqtMenu;
import com.zhiqiantong.ucenter.model.po.ZqtUser;
import com.zhiqiantong.ucenter.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mr.M
 * @version 1.0
 * @description TODO
 * @date 2022/9/28 18:09
 */
@Service
@Slf4j
public class UserServiceImpl implements UserDetailsService {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    ZqtUserMapper zqtUserMapper;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    ZqtMenuMapper menuMapper;


    /**
     * @description 查询用户信息组成用户身份信息
     * @param s  AuthParamsDto类型的json数据
     * @return org.springframework.security.core.userdetails.UserDetails
     * @author Mr.M
     * @date 2022/9/28 18:30
     */
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        AuthParamsDto authParamsDto = null;
        try {
            //将认证参数转为AuthParamsDto类型
            authParamsDto = JSON.parseObject(s, AuthParamsDto.class);
        } catch (Exception e) {
            log.info("认证请求不符合项目要求:{}",s);
            throw new RuntimeException("认证请求数据格式不对");
        }

        //认证方法
        String authType = authParamsDto.getAuthType();
        AuthService authService =  applicationContext.getBean(authType + "_authservice",AuthService.class);
        ZqtUserExt user = authService.execute(authParamsDto);

        return getUserPrincipal(user);

    }

    /**
     * @description 查询用户信息
     * @param user  用户id，主键
     * @return com.xuecheng.ucenter.model.po.XcUser 用户信息
     * @author Mr.M
     * @date 2022/9/29 12:19
     */
    public UserDetails getUserPrincipal(ZqtUserExt user){
        String password = user.getPassword();

        //查询用户权限
        List<ZqtMenu> zqtMenus = menuMapper.selectPermissionByUserId(user.getId());
        List<String> permissions = new ArrayList<>();
        if(zqtMenus.size() <= 0){
            //用户权限,如果不加则报Cannot pass a null GrantedAuthority collection
            permissions.add("p1");
        }else{
            zqtMenus.forEach(menu->{
                permissions.add(menu.getCode());
            });
        }
        //将用户权限放在XcUserExt中
        user.setPermissions(permissions);

        //为了安全在令牌中不放密码
        user.setPassword(null);
        //将user对象转json
        String userString = JSON.toJSONString(user);

        String[] authorities = permissions.toArray(new String[0]);

        //创建UserDetails对象
        return User.withUsername(userString).password(password ).authorities(authorities).build();
    }

}
