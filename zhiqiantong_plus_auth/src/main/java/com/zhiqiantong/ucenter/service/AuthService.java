package com.zhiqiantong.ucenter.service;

import com.zhiqiantong.ucenter.model.dto.AuthParamsDto;
import com.zhiqiantong.ucenter.model.dto.ZqtUserExt;

/**
 * @description 认证service
 * @author Mr.M
 * @date 2022/9/29 12:10
 * @version 1.0
 */
public interface AuthService {

   /**
    * @description 认证方法
    * @param authParamsDto 认证参数
    * @return com.xuecheng.ucenter.model.po.XcUser 用户信息
    * @author Mr.M
    * @date 2022/9/29 12:11
   */
   ZqtUserExt execute(AuthParamsDto authParamsDto);

}
