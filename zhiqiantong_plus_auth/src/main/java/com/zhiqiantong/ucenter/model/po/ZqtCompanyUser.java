package com.zhiqiantong.ucenter.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author itcast
 */
@Data
@TableName("zqt_company_user")
public class ZqtCompanyUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String companyId;

    private String userId;


}
