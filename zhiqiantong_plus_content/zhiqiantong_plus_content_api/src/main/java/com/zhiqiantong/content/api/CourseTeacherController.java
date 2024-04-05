package com.zhiqiantong.content.api;

import com.zhiqiantong.content.model.dto.TeachplanDto;
import com.zhiqiantong.content.model.po.CourseTeacher;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.web.bind.annotation.*;
import com.zhiqiantong.content.service.CourseTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.zhiqiantong.content.utils.common.ContentCommon.COMPANY_ID;

/**
 * <p>
 * 课程-教师关系表 前端控制器
 * </p>
 *
 * @author zxg
 */
@Slf4j
@RestController
@RequestMapping("/courseTeacher")
@Api(value = "师资信息编辑接口",tags = "师资信息编辑接口")
public class CourseTeacherController {

    @Autowired
    private CourseTeacherService  courseTeacherService;

    @ApiOperation("查询课程师资列表")
    @ApiImplicitParam(value = "courseId",name = "课程Id",required = true,dataType = "Long",paramType = "path")
    @GetMapping("/list/{courseId}")
    public List<CourseTeacher> getCourseTeacherList(@PathVariable Long courseId){
        return courseTeacherService.getCourseTeacherList(courseId);
    }

    @ApiOperation("保存课程师资信息")
    @PostMapping("")
    public CourseTeacher saveCourseTeacher(@RequestBody CourseTeacher courseTeacher){
        return courseTeacherService.saveCourseTeacher(COMPANY_ID,courseTeacher);
    }

    @ApiOperation("删除课程师资信息")
    @DeleteMapping("course/{courseId}/{courseTeacherId}")
    public void deleteCourseTeacher(@PathVariable Long courseId,@PathVariable Long courseTeacherId){
        courseTeacherService.deleteCourseTeacher(COMPANY_ID,courseId,courseTeacherId);
    }

}
