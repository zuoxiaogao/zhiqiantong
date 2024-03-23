package com.zhiqiantong.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhiqiantong.content.model.dto.TeachplanDto;
import com.zhiqiantong.content.model.po.Teachplan;

import java.util.List;

/**
 * <p>
 * 课程计划 服务类
 * </p>
 *
 * @author zxg
 * @since 2024-03-20
 */
public interface TeachplanService extends IService<Teachplan> {

    List<TeachplanDto> getTreeNodes(Long courseId);
}
