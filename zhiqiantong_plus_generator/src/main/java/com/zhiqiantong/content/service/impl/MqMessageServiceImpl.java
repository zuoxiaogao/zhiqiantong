package com.zhiqiantong.content.service.impl;

import com.zhiqiantong.content.model.po.MqMessage;
import com.zhiqiantong.content.mapper.MqMessageMapper;
import com.zhiqiantong.content.service.MqMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zxg
 */
@Slf4j
@Service
public class MqMessageServiceImpl extends ServiceImpl<MqMessageMapper, MqMessage> implements MqMessageService {

}
