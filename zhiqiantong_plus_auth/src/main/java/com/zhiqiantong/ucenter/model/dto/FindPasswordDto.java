package com.zhiqiantong.ucenter.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class FindPasswordDto implements Serializable {
    private String cellphone;
    private String email;
    private String checkcodekey;
    private String checkcode;
    private String confirmpwd;
    private String password;
}
