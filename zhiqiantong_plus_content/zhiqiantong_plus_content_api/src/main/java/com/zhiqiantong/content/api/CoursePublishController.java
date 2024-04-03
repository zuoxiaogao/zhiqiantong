package com.zhiqiantong.content.api;

import com.zhiqiantong.content.model.dto.CoursePreviewDto;
import com.zhiqiantong.content.model.po.CoursePublish;
import org.springframework.web.bind.annotation.*;
import com.zhiqiantong.content.service.CoursePublishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 课程发布 前端控制器
 * </p>
 *
 * @author zxg
 */
@Slf4j
@RestController
@Api(value = "课程预览发布接口",tags = "课程预览发布接口")
public class CoursePublishController {

    @Autowired
    private CoursePublishService  coursePublishService;

    @GetMapping("/coursepreview/{courseId}")
    public ModelAndView preview(@PathVariable("courseId") Long courseId){

        //获取课程预览信息
        CoursePreviewDto coursePreviewInfo = coursePublishService.getCoursePreviewInfo(courseId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("model",coursePreviewInfo);
        modelAndView.setViewName("course_template");
        return modelAndView;
    }

    @PostMapping("/courseaudit/commit/{courseId}")
    public void commitAudit(@PathVariable("courseId") Long courseId){
        Long companyId = 1232141425L;
        coursePublishService.commitAudit(companyId,courseId);
    }

    @ApiOperation("课程发布")
    @PostMapping ("/coursepublish/{courseId}")
    public void coursePublish(@PathVariable("courseId") Long courseId){
        Long companyId = 1232141425L;
        coursePublishService.publish(companyId,courseId);
    }

    @ApiOperation("课程下架")
    @GetMapping ("/courseoffline/{courseId}")
    public void courseOffline(@PathVariable("courseId") Long courseId){
        Long companyId = 1232141425L;
        coursePublishService.offline(companyId,courseId);
    }

    @ApiOperation("查询课程发布信息")
    @GetMapping("/r/coursepublish/{courseId}")
    public CoursePublish getCoursepublish(@PathVariable("courseId") Long courseId) {
        return coursePublishService.getCoursePublish(courseId);
    }


}
