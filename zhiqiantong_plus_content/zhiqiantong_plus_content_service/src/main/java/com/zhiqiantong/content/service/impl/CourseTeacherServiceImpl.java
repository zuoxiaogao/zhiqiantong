package com.zhiqiantong.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhiqiantong.base.execption.ZhiQianTongPlusException;
import com.zhiqiantong.content.mapper.CourseBaseMapper;
import com.zhiqiantong.content.mapper.CourseTeacherMapper;
import com.zhiqiantong.content.model.po.CourseBase;
import com.zhiqiantong.content.model.po.CourseTeacher;
import com.zhiqiantong.content.service.CourseTeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.zhiqiantong.content.utils.common.ContentCommon.ADD_COURSE_INIT_AUDIT_STATUS;
import static com.zhiqiantong.content.utils.common.ContentCommon.COMPANY_ID;

/**
 * <p>
 * 课程-教师关系表 服务实现类
 * </p>
 *
 * @author zxg
 */
@Slf4j
@Service
public class CourseTeacherServiceImpl extends ServiceImpl<CourseTeacherMapper, CourseTeacher> implements CourseTeacherService {

    @Autowired
    private CourseTeacherMapper courseTeacherMapper;
    @Autowired
    private CourseBaseMapper courseBaseMapper;

    @Override
    public List<CourseTeacher> getCourseTeacherList(Long courseId) {
        LambdaQueryWrapper<CourseTeacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseTeacher::getCourseId,courseId);
        return courseTeacherMapper.selectList(queryWrapper);
    }

    @Override
    public CourseTeacher saveCourseTeacher(Long companyId, CourseTeacher courseTeacher) {
        if (companyId == null || !companyId.equals(COMPANY_ID)) {
            ZhiQianTongPlusException.cast("你不能对其他机构的课程进行操作，请稍后再试！");
        }
        if (courseTeacher.getId() == null) {
            courseTeacher.setCreateDate(LocalDateTime.now());
            int insert = courseTeacherMapper.insert(courseTeacher);
            if (insert <= 0) {
                ZhiQianTongPlusException.cast("添加教师失败，请稍后重试！");
            }
            return courseTeacher;
        }
        int i = courseTeacherMapper.updateById(courseTeacher);
        if (i <= 0) {
            ZhiQianTongPlusException.cast("修改教师信息失败，请稍后重试!");
        }
        return courseTeacher;
    }

    @Override
    public void deleteCourseTeacher(Long companyId, Long courseId, Long courseTeacherId) {
        if (companyId == null || !companyId.equals(COMPANY_ID)) {
            ZhiQianTongPlusException.cast("你不能对其他机构的课程进行操作，请稍后再试！");
        }
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if (courseBase == null) {
            ZhiQianTongPlusException.cast("课程信息为空，请稍后重试！");
        }
        if (!courseBase.getAuditStatus().equals(ADD_COURSE_INIT_AUDIT_STATUS)) {
            ZhiQianTongPlusException.cast("课程已经送往审核，请稍后重试！");
        }
        int i = courseTeacherMapper.deleteById(courseTeacherId);
        if (i <= 0) {
            ZhiQianTongPlusException.cast("删除教师信息失败，请稍后重试！");
        }
    }
}
