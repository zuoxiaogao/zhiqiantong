package com.zhiqiantong.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhiqiantong.base.model.PageParams;
import com.zhiqiantong.base.model.PageResult;
import com.zhiqiantong.content.model.dto.QueryCourseParamsDto;
import com.zhiqiantong.content.model.po.CourseBase;

/**
 * <p>
 * 课程基本信息 服务类
 * </p>
 *
 * @author zxg
 * @since 2024-03-20
 */
public interface CourseBaseService extends IService<CourseBase> {
    PageResult<CourseBase> queryAllList(PageParams pageParams, QueryCourseParamsDto queryCourseParams);
}
