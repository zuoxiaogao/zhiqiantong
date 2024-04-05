package com.zhiqiantong.orders.model.dto;

import com.zhiqiantong.orders.model.po.ZqtPayRecord;
import lombok.Data;
import lombok.ToString;

/**
 * @author Mr.M
 * @version 1.0
 * @description 支付记录dto
 * @date 2022/10/4 11:30
 */
@Data
@ToString
public class PayRecordDto extends ZqtPayRecord {

    //二维码
    private String qrcode;

}
