package com.zhiqiantong.content.api;

import com.zhiqiantong.base.execption.ValidationGroups;
import com.zhiqiantong.base.model.PageParams;
import com.zhiqiantong.base.model.PageResult;
import com.zhiqiantong.content.model.dto.*;
import com.zhiqiantong.content.model.po.CourseBase;
import com.zhiqiantong.content.service.CourseBaseService;
import com.zhiqiantong.content.service.CoursePublishService;
import com.zhiqiantong.content.util.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.zhiqiantong.content.utils.common.ContentCommon.COMPANY_ID;

@Slf4j
@RestController
@Api(value = "课程信息编辑接口",tags = "课程信息编辑接口")
@RequestMapping("/course")
public class CourseBaseController {
    @Autowired
    private CourseBaseService courseBaseService;

    @Autowired
    private CoursePublishService coursePublishService;


    @ApiOperation("所有课程查询接口")
    @PreAuthorize("hasAuthority('xc_teachmanager_course_list')")
    @PostMapping("/list")
    public PageResult<CourseBase> list(PageParams pageParams,
        @RequestBody QueryCourseParamsDto queryCourseParams) {
        //取出用户身份
        SecurityUtil.ZqtUser user = SecurityUtil.getUser();
        //机构id
        String companyId = null;
        if (user != null) {
            companyId = user.getCompanyId();
        }
        return courseBaseService.queryAllList(Long.parseLong(companyId == null ? "1" : companyId),pageParams,queryCourseParams);
    }

    @ApiOperation("新增课程基础信息接口")
    @PostMapping("")
    public CourseBaseInfoDto createCourseBase(@RequestBody @Validated({ValidationGroups.Insert.class}) AddCourseDto addCourseDto){
        return courseBaseService.createCourseBase(COMPANY_ID,addCourseDto);
    }

    @ApiOperation("根据课程id查询接口")
    @GetMapping("/{courseId}")
    public CourseBaseInfoDto getCourseBaseById(@PathVariable Long courseId){
        //取出当前用户身份
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SecurityUtil.ZqtUser user = SecurityUtil.getUser();
        System.out.println(user);

        return courseBaseService.getCourseBaseById(COMPANY_ID,courseId);
    }

    @ApiOperation("修改课程基础信息接口")
    @PutMapping("")
    public CourseBaseInfoDto modifyCourseBase(@RequestBody @Validated({ValidationGroups.Update.class}) EditCourseDto editCourseDto){
        return courseBaseService.modifyCourseBase(COMPANY_ID,editCourseDto);
    }

    @ApiOperation("删除课程基础信息接口")
    @DeleteMapping("{courseId}")
    public void deleteCourseBase(@PathVariable Long courseId){
        courseBaseService.deleteCourseBase(COMPANY_ID,courseId);
    }

    @ApiOperation("获取课程发布信息")
    @GetMapping("/whole/{courseId}")
    public CoursePreviewDto getPreviewInfo(@PathVariable("courseId") Long courseId) {
        //获取课程预览信息
        return coursePublishService.getCoursePreviewInfo(courseId);
    }
}
