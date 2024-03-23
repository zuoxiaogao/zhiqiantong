package com.zhiqiantong.content.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zhiqiantong.content.service.CourseTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 课程-教师关系表 前端控制器
 * </p>
 *
 * @author zxg
 */
@Slf4j
@RestController
@RequestMapping("/teachplan")
@Api(value = "课程分类信息编辑接口",tags = "课程分类信息编辑接口")
public class CourseTeacherController {

    @Autowired
    private CourseTeacherService  courseTeacherService;


}
