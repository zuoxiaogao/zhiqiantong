package com.zhiqiantong.learning.api;

import com.zhiqiantong.base.execption.ZhiQianTongPlusException;
import com.zhiqiantong.base.model.PageResult;
import com.zhiqiantong.learning.model.dto.MyCourseTableParams;
import com.zhiqiantong.learning.model.dto.ZqtChooseCourseDto;
import com.zhiqiantong.learning.model.dto.ZqtCourseTablesDto;
import com.zhiqiantong.learning.model.po.ZqtCourseTables;
import com.zhiqiantong.learning.service.MyCourseTablesService;
import com.zhiqiantong.learning.util.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mr.M
 * @version 1.0
 * @description 我的课程表接口
 * @date 2022/10/25 9:40
 */

@Api(value = "我的课程表接口", tags = "我的课程表接口")
@Slf4j
@RestController
public class MyCourseTablesController {

    @Autowired
    private MyCourseTablesService courseTablesService;


    @ApiOperation("添加选课")
    @PostMapping("/choosecourse/{courseId}")
    public ZqtChooseCourseDto addChooseCourse(@PathVariable("courseId") Long courseId) {

        //登录用户
        SecurityUtil.ZqtUser user = SecurityUtil.getUser();
        if(user == null){
            ZhiQianTongPlusException.cast("请登录后继续选课");
        }
        String userId = user.getId();
        return  courseTablesService.addChooseCourse(userId, courseId);

    }

    @ApiOperation("查询学习资格")
    @PostMapping("/choosecourse/learnstatus/{courseId}")
    public ZqtCourseTablesDto getLearnStatus(@PathVariable("courseId") Long courseId) {

        //登录用户
        SecurityUtil.ZqtUser user = SecurityUtil.getUser();
        if(user == null){
            ZhiQianTongPlusException.cast("请登录后继续选课");
        }
        String userId = user.getId();
        return  courseTablesService.getLearningStatus(userId, courseId);

    }

    @ApiOperation("我的课程表")
    @GetMapping("/mycoursetable")
    public PageResult<ZqtCourseTables> mycoursetable(MyCourseTableParams params) {

        //登录用户
        SecurityUtil.ZqtUser user = SecurityUtil.getUser();
        if(user == null){
            ZhiQianTongPlusException.cast("请登录后继续选课");
        }
        String userId = user.getId();
        //设置当前的登录用户
        params.setUserId(userId);

        return courseTablesService.mycourestabls(params);

    }

}
