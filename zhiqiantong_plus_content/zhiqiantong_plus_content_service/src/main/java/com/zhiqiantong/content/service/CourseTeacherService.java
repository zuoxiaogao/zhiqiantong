package com.zhiqiantong.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhiqiantong.content.model.po.CourseTeacher;

import java.util.List;

/**
 * <p>
 * 课程-教师关系表 服务类
 * </p>
 *
 * @author zxg
 * @since 2024-03-20
 */
public interface CourseTeacherService extends IService<CourseTeacher> {

    List<CourseTeacher> getCourseTeacherList(Long courseId);

    CourseTeacher saveCourseTeacher(Long companyId, CourseTeacher courseTeacher);

    void deleteCourseTeacher(Long companyId, Long courseId, Long courseTeacherId);
}
