package com.zhiqiantong.content.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhiqiantong.content.mapper.TeachplanMapper;
import com.zhiqiantong.content.model.dto.TeachplanDto;
import com.zhiqiantong.content.model.po.Teachplan;
import com.zhiqiantong.content.service.TeachplanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 课程计划 服务实现类
 * </p>
 *
 * @author zxg
 */
@Slf4j
@Service
public class TeachplanServiceImpl extends ServiceImpl<TeachplanMapper, Teachplan> implements TeachplanService {

    @Autowired
    private TeachplanMapper teachplanMapper;

    @Override
    public List<TeachplanDto> getTreeNodes(Long courseId) {
        return teachplanMapper.selectTreeNodes(courseId);
    }
}
