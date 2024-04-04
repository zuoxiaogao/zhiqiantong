package com.zhiqiantong.orders.service;

import com.zhiqiantong.messagesdk.model.po.MqMessage;
import com.zhiqiantong.orders.model.dto.AddOrderDto;
import com.zhiqiantong.orders.model.dto.PayRecordDto;
import com.zhiqiantong.orders.model.dto.PayStatusDto;
import com.zhiqiantong.orders.model.po.ZqtPayRecord;

public interface OrderService {

    /**
     * @param addOrderDto 订单信息
     * @return PayRecordDto 支付交易记录(包括二维码)
     * @description 创建商品订单
     * @author Mr.M
     * @date 2022/10/4 11:02
     */
    PayRecordDto createOrder(String userId, AddOrderDto addOrderDto);

    /**
     * @description 查询支付交易记录
     * @param payNo  交易记录号
     * @return com.zhiqiantong.orders.model.po.ZqtPayRecord
     * @author Mr.M
     * @date 2022/10/20 23:38
     */
    ZqtPayRecord getPayRecordByPayno(String payNo);

    /**
     * 请求支付宝查询支付结果
     * @param payNo 支付记录id
     * @return 支付记录信息
     */
    PayRecordDto queryPayResult(String payNo);

    /**
     * @description 保存支付宝支付结果
     * @param payStatusDto  支付结果信息
     * @return void
     * @author Mr.M
     * @date 2022/10/4 16:52
     */
    void saveAliPayStatus(PayStatusDto payStatusDto) ;

    /**
     * 发送通知结果
     * @param message
     */
    void notifyPayResult(MqMessage message);

}
