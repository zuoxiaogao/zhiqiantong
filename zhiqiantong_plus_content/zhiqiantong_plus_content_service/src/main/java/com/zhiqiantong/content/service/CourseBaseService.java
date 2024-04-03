package com.zhiqiantong.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhiqiantong.base.model.PageParams;
import com.zhiqiantong.base.model.PageResult;
import com.zhiqiantong.content.model.dto.AddCourseDto;
import com.zhiqiantong.content.model.dto.CourseBaseInfoDto;
import com.zhiqiantong.content.model.dto.EditCourseDto;
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
    PageResult<CourseBase> queryAllList(long companyId, PageParams pageParams, QueryCourseParamsDto queryCourseParams);

    CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto addCourseDto);

    CourseBaseInfoDto getCourseBaseById(Long companyId, Long courseId);

    CourseBaseInfoDto modifyCourseBase(Long companyId, EditCourseDto editCourseDto);

    void deleteCourseBase(Long companyId, Long courseId);

    CourseBaseInfoDto getCourseBaseInfo(long courseId);
}
