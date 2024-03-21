package com.zhiqiantong.content.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhiqiantong.base.model.PageParams;
import com.zhiqiantong.base.model.PageResult;
import com.zhiqiantong.content.mapper.CourseBaseMapper;
import com.zhiqiantong.content.model.dto.QueryCourseParamsDto;
import com.zhiqiantong.content.model.po.CourseBase;
import com.zhiqiantong.content.service.CourseBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 课程基本信息 服务实现类
 * </p>
 *
 * @author zxg
 */
@Slf4j
@Service
public class CourseBaseServiceImpl extends ServiceImpl<CourseBaseMapper, CourseBase> implements CourseBaseService {

    @Autowired
    CourseBaseMapper courseBaseMapper;

    @Override
    public PageResult<CourseBase> queryAllList(PageParams pageParams, QueryCourseParamsDto queryCourseParams) {
        List<CourseBase> courseBases = courseBaseMapper.selectList(null);
        Integer count = courseBaseMapper.selectCount(null);
        return new PageResult<>(courseBases,count,pageParams.getPageNo(),pageParams.getPageSize());
    }
}
