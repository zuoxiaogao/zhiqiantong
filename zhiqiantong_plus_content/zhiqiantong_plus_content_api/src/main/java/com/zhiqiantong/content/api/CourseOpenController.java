package com.zhiqiantong.content.api;

import com.zhiqiantong.content.model.dto.CoursePreviewDto;
import com.zhiqiantong.content.service.CourseBaseService;
import com.zhiqiantong.content.service.CoursePublishService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "课程公开查询接口", tags = "课程公开查询接口")
@RestController
@RequestMapping("/open")
public class CourseOpenController {

    @Autowired
    private CourseBaseService courseBaseInfoService;

}
