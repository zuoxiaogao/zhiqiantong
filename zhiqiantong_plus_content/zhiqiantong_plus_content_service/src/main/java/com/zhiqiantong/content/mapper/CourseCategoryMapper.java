package com.zhiqiantong.content.mapper;

import com.zhiqiantong.content.model.dto.CourseCategoryTreeDto;
import com.zhiqiantong.content.model.po.CourseCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 课程分类 Mapper 接口
 * </p>
 *
 * @author zxg
 */
public interface CourseCategoryMapper extends BaseMapper<CourseCategory> {

    public List<CourseCategoryTreeDto> selectTreeNodes(String id);

}
