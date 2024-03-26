package com.zhiqiantong.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhiqiantong.base.execption.ZhiQianTongPlusException;
import com.zhiqiantong.content.mapper.CourseBaseMapper;
import com.zhiqiantong.content.mapper.TeachplanMapper;
import com.zhiqiantong.content.mapper.TeachplanMediaMapper;
import com.zhiqiantong.content.model.dto.SaveTeachplanDto;
import com.zhiqiantong.content.model.dto.TeachplanDto;
import com.zhiqiantong.content.model.po.CourseBase;
import com.zhiqiantong.content.model.po.Teachplan;
import com.zhiqiantong.content.model.po.TeachplanMedia;
import com.zhiqiantong.content.service.TeachplanService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.zhiqiantong.content.utils.common.ContentCommon.ADD_COURSE_INIT_AUDIT_STATUS;

/**
 * <p>
 * 课程计划 服务实现类
 * </p>
 *
 * @author zxg
 */
@Slf4j
@Service
public class TeachplanServiceImpl extends ServiceImpl<TeachplanMapper, Teachplan> implements TeachplanService {

    @Autowired
    private TeachplanMapper teachplanMapper;
    @Autowired
    private CourseBaseMapper courseBaseMapper;
    @Autowired
    private TeachplanMediaMapper teachplanMediaMapper;

    @Override
    public List<TeachplanDto> getTreeNodes(Long courseId) {
        return teachplanMapper.selectTreeNodes(courseId);
    }

    @Transactional
    @Override
    public void saveTeachplan(SaveTeachplanDto teachplanDto) {
        if (teachplanDto == null) {
            ZhiQianTongPlusException.cast("参数异常，请稍后重试！");
        }
        Long courseId = teachplanDto.getCourseId();
        if (courseId == null || StringUtils.isBlank(courseId.toString())) {
            ZhiQianTongPlusException.cast("参数异常，请稍后重试！");
        }
        Long teachplanId = teachplanDto.getId();
        if (teachplanId == null) {
            //取出同父同级别的课程计划数量
            int count = getTeachplanCount(courseId,teachplanDto.getParentid());
            Teachplan teachplan = new Teachplan();
            teachplan.setOrderby(count+1);
            BeanUtils.copyProperties(teachplanDto,teachplan);
            teachplan.setCreateDate(LocalDateTime.now());
            int insert = teachplanMapper.insert(teachplan);
            if (insert <= 0) {
                ZhiQianTongPlusException.cast("添加课程失败，请稍后重试！");
            }
        } else {
            Teachplan teachplan = teachplanMapper.selectById(teachplanId);
            if (teachplan == null) {
                ZhiQianTongPlusException.cast("课程计划查询为空，请稍后重试！");
            }
            BeanUtils.copyProperties(teachplanDto,teachplan);
            teachplan.setChangeDate(LocalDateTime.now());
            int i = teachplanMapper.updateById(teachplan);
            if (i <= 0) {
                ZhiQianTongPlusException.cast("修改课程计划失败，请稍后重试！");
            }
        }
    }

    @Transactional
    @Override
    public void deletedTeachplanById(Long teachplanId) {
        Teachplan teachplan = teachplanMapper.selectById(teachplanId);
        if (teachplan == null) {
            ZhiQianTongPlusException.cast("课程计划不存在，请稍后重试！");
        }
        Long courseId = teachplan.getCourseId();
        if (courseId == null || StringUtils.isBlank(courseId.toString())) {
            ZhiQianTongPlusException.cast("课程id为空，请稍后重试！");
        }
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if (courseBase == null) {
            ZhiQianTongPlusException.cast("课程基本信息为空，请稍后重试！");
        }
        if (!courseBase.getAuditStatus().equals(ADD_COURSE_INIT_AUDIT_STATUS)) {
            ZhiQianTongPlusException.cast("课程已经送往审核，不能删除课程相关信息！");
        }
        Integer grade = teachplan.getGrade();
        if (grade == 1) {
            LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Teachplan::getParentid,teachplanId);
            queryWrapper.eq(Teachplan::getCourseId,courseId);
            Integer integer = teachplanMapper.selectCount(queryWrapper);
            if (integer != 0) {
                ZhiQianTongPlusException.cast("课程计划信息还有子级信息，无法操作");
            }
            int i = teachplanMapper.deleteById(teachplanId);
            if (i <= 0) {
                ZhiQianTongPlusException.cast("删除课程计划出现了异常，请稍后重试");
            }
            return;
        }
        LambdaQueryWrapper<TeachplanMedia> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TeachplanMedia::getTeachplanId,teachplanId);
        teachplanMediaMapper.delete(queryWrapper);
        int i = teachplanMapper.deleteById(teachplanId);
        if (i <= 0) {
            ZhiQianTongPlusException.cast("删除课程计划出现了异常，请稍后重试！");
        }
    }

    @Transactional
    @Override
    public void moveUp(Long teachplanId) {
        upOrDownMove(teachplanId,"up");
    }

    @Transactional
    @Override
    public void moveDown(Long teachplanId) {
        upOrDownMove(teachplanId,"down");
    }

    private int getTeachplanCount(Long courseId, Long parentid) {
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getCourseId,courseId);
        queryWrapper.eq(Teachplan::getParentid,parentid);
        return teachplanMapper.selectCount(queryWrapper);
    }

    private void upOrDownMove (Long teachplanId, String upOrDown) {
        Teachplan teachplan = teachplanMapper.selectById(teachplanId);
        if (teachplan == null) {
            ZhiQianTongPlusException.cast("课程计划为空，请稍后重试！");
        }
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getCourseId,teachplan.getCourseId());
        queryWrapper.eq(Teachplan::getGrade,teachplan.getGrade());
        queryWrapper.eq(Teachplan::getParentid,teachplan.getParentid());
        if (upOrDown.equals("up")) {
            queryWrapper.lt(Teachplan::getOrderby,teachplan.getOrderby());
            queryWrapper.orderByDesc(Teachplan::getOrderby);
        } else if (upOrDown.equals("down")) {
            queryWrapper.gt(Teachplan::getOrderby,teachplan.getOrderby());
            queryWrapper.orderByAsc(Teachplan::getOrderby);
        } else {
            return;
        }
        queryWrapper.last(" limit 1");
        Teachplan teachplanTemp = teachplanMapper.selectOne(queryWrapper);
        if (teachplanTemp == null) return;
        Integer orderby = teachplan.getOrderby();
        teachplan.setOrderby(teachplanTemp.getOrderby());
        teachplanTemp.setOrderby(orderby);
        int i = teachplanMapper.updateById(teachplan);
        int i1 = teachplanMapper.updateById(teachplanTemp);
        if (i <= 0 || i1 <= 0) {
            if (upOrDown.equals("up")) {
                ZhiQianTongPlusException.cast("上移出现了错误，请稍后重试！");
            } else {
                ZhiQianTongPlusException.cast("下移出现了错误，请稍后重试！");
            }
        }
    }
}
