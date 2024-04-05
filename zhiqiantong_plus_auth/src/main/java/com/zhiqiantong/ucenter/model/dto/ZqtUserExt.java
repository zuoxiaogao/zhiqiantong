package com.zhiqiantong.ucenter.model.dto;

import com.zhiqiantong.ucenter.model.po.ZqtUser;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @description 用户扩展信息
 * @author Mr.M
 * @date 2022/9/30 13:56
 * @version 1.0
 */
@Data
public class ZqtUserExt extends ZqtUser {
    //用户权限
    List<String> permissions = new ArrayList<>();
}
