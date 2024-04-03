package com.zhiqiantong.ucenter.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class RegisterParamDto implements Serializable {
    private String cellphone;
    private String username;
    private String email;
    private String nickname;
    private String password;
    private String confirmpwd;
    private String checkcodekey;
    private String checkcode;
}
