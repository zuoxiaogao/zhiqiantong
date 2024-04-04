package com.zhiqiantong.content;

import com.zhiqiantong.base.model.PageParams;
import com.zhiqiantong.base.model.PageResult;
import com.zhiqiantong.content.model.dto.QueryCourseParamsDto;
import com.zhiqiantong.content.model.po.CourseBase;
import com.zhiqiantong.content.service.CourseBaseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CourseBaseInfoServiceTests {

    @Autowired
    CourseBaseService courseBaseService;

    @Test
    void testCourseBaseInfoService() {
        //查询条件
        QueryCourseParamsDto queryCourseParamsDto = new QueryCourseParamsDto();
        queryCourseParamsDto.setCourseName("java");
        queryCourseParamsDto.setAuditStatus("202004");
        queryCourseParamsDto.setPublishStatus("203001");

        //分页参数
        PageParams pageParams = new PageParams();
        pageParams.setPageNo(1L);//页码
        pageParams.setPageSize(3L);//每页记录数

        PageResult<CourseBase> courseBasePageResult = courseBaseService.queryAllList(Long.parseLong("1"), pageParams, queryCourseParamsDto);
        System.out.println(courseBasePageResult);
    }

}
