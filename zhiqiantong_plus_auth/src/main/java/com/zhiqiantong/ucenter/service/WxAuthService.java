package com.zhiqiantong.ucenter.service;

import com.zhiqiantong.ucenter.model.po.ZqtUser;

/**
 * @author Mr.M
 * @version 1.0
 * @description 微信认证接口
 * @date 2023/2/21 22:15
 */
public interface WxAuthService {

    ZqtUser wxAuth(String code);

}
