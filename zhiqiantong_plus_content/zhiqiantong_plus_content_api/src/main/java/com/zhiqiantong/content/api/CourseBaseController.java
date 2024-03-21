package com.zhiqiantong.content.api;

import com.zhiqiantong.base.model.PageParams;
import com.zhiqiantong.base.model.PageResult;
import com.zhiqiantong.content.model.dto.QueryCourseParamsDto;
import com.zhiqiantong.content.model.po.CourseBase;
import com.zhiqiantong.content.service.CourseBaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@Api(value = "课程信息编辑接口",tags = "课程信息编辑接口")
@RequestMapping("/course")
public class CourseBaseController {
    @Autowired
    private CourseBaseService courseBaseService;


    @ApiOperation("课程查询接口")
    @PostMapping("list")
    public PageResult<CourseBase> list(PageParams pageParams,
        @RequestBody QueryCourseParamsDto queryCourseParams) {

        return courseBaseService.queryAllList(pageParams,queryCourseParams);

    }


}
