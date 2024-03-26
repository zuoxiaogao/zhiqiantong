package com.zhiqiantong.content.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhiqiantong.base.execption.ZhiQianTongPlusException;
import com.zhiqiantong.base.model.PageParams;
import com.zhiqiantong.base.model.PageResult;
import com.zhiqiantong.content.mapper.*;
import com.zhiqiantong.content.model.dto.AddCourseDto;
import com.zhiqiantong.content.model.dto.CourseBaseInfoDto;
import com.zhiqiantong.content.model.dto.EditCourseDto;
import com.zhiqiantong.content.model.dto.QueryCourseParamsDto;
import com.zhiqiantong.content.model.po.*;
import com.zhiqiantong.content.service.CourseBaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.zhiqiantong.content.utils.common.ContentCommon.*;

/**
 * <p>
 * 课程基本信息 服务实现类
 * </p>
 *
 * @author zxg
 */
@Slf4j
@Service
public class CourseBaseServiceImpl extends ServiceImpl<CourseBaseMapper, CourseBase> implements CourseBaseService {

    @Autowired
    private CourseBaseMapper courseBaseMapper;
    @Autowired
    private CourseMarketMapper courseMarketMapper;
    @Autowired
    private TeachplanMapper teachplanMapper;
    @Autowired
    private CourseTeacherMapper courseTeacherMapper;
    @Autowired
    private TeachplanMediaMapper teachplanMediaMapper;
    @Autowired
    private CourseCategoryMapper courseCategoryMapper;

    @Override
    public PageResult<CourseBase> queryAllList(PageParams pageParams, QueryCourseParamsDto queryCourseParams) {
        //构建查询条件对象
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();
        //构建查询条件，根据课程名称查询
        queryWrapper.like(StringUtils.isNotEmpty(queryCourseParams.getCourseName()),CourseBase::getName,queryCourseParams.getCourseName());
        //构建查询条件，根据课程审核状态查询
        queryWrapper.eq(StringUtils.isNotEmpty(queryCourseParams.getAuditStatus()),CourseBase::getAuditStatus,queryCourseParams.getAuditStatus());
        //构建查询条件，根据课程发布状态查询
        queryWrapper.eq(StringUtils.isNotEmpty(queryCourseParams.getPublishStatus()),CourseBase::getStatus,queryCourseParams.getPublishStatus());

        //分页对象
        Page<CourseBase> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        // 查询数据内容获得结果
        Page<CourseBase> pageResult = courseBaseMapper.selectPage(page, queryWrapper);
        // 获取数据列表
        List<CourseBase> list = pageResult.getRecords();
        // 获取数据总数
        long total = pageResult.getTotal();
        // 构建结果集
        return new PageResult<>(list, total, pageParams.getPageNo(), pageParams.getPageSize());

    }

    @Override
    @Transactional
    public CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto addCourseDto) {
        //添加基本课程参数校验
        courseBaseParamCheck(addCourseDto);
        //新增对象
        CourseBase courseBase = new CourseBase();
        BeanUtils.copyProperties(addCourseDto,courseBase);
        courseBase.setStatus(ADD_COURSE_INIT_RELEASE_STATUS);
        courseBase.setAuditStatus(ADD_COURSE_INIT_AUDIT_STATUS);
        courseBase.setCreateDate(LocalDateTime.now());
        courseBase.setCompanyId(companyId);

        int insert = courseBaseMapper.insert(courseBase);
        if (insert <= 0) {
            throw new ZhiQianTongPlusException("新增课程基本信息失败");
        }

        Long courseId = courseBase.getId();

        CourseMarket courseMarket = new CourseMarket();
        BeanUtils.copyProperties(addCourseDto,courseMarket);
        courseMarket.setId(courseId);

        int i = saveCourseMarket(courseMarket);
        if (i <= 0) {
            throw new ZhiQianTongPlusException("保存课程营销信息失败");
        }

        return getCourseBaseInfo(courseId);
    }

    @Override
    public CourseBaseInfoDto getCourseBaseById(Long companyId, Long courseId) {
        if (courseId == null || StringUtils.isBlank(Long.toString(courseId))) {
            throw new ZhiQianTongPlusException("课程id不能为空");
        }
        CourseBaseInfoDto courseBaseInfo = getCourseBaseInfo(courseId);
        if (courseBaseInfo == null) {
            throw new ZhiQianTongPlusException("查询不到课程信息，请稍后重试！");
        }
        if (companyId == null || !companyId.equals(courseBaseInfo.getCompanyId())) {
            throw new ZhiQianTongPlusException("出现错误，你不能操作其他机构的课程。");
        }
        return courseBaseInfo;
    }

    @Transactional
    @Override
    public CourseBaseInfoDto modifyCourseBase(Long companyId, EditCourseDto editCourseDto) {
        courseBaseParamCheck(editCourseDto);
        if (editCourseDto.getId() == null || StringUtils.isBlank(Long.toString(editCourseDto.getId()))) {
            throw new ZhiQianTongPlusException("课程id为空，请稍后重试！");
        }
        //课程id
        Long courseId = editCourseDto.getId();
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if(courseBase==null){
            ZhiQianTongPlusException.cast("课程不存在");
        }

        //校验本机构只能修改本机构的课程
        if(companyId == null || !companyId.equals(courseBase.getCompanyId())){
            ZhiQianTongPlusException.cast("本机构只能修改本机构的课程");
        }

        //封装基本信息的数据
        BeanUtils.copyProperties(editCourseDto,courseBase);
        courseBase.setChangeDate(LocalDateTime.now());

        //更新课程基本信息
        int i = courseBaseMapper.updateById(courseBase);
        if (i <= 0) {
            ZhiQianTongPlusException.cast("更新课程信息出现错误，请稍后重试！");
        }

        //封装营销信息的数据
        CourseMarket courseMarket = new CourseMarket();
        BeanUtils.copyProperties(editCourseDto,courseMarket);
        int i1 = saveCourseMarket(courseMarket);
        if (i1 <= 0) {
            ZhiQianTongPlusException.cast("更新课程信息出现错误");
        }

        //查询课程信息
        return this.getCourseBaseInfo(courseId);
    }

    @Transactional
    @Override
    public void deleteCourseBase(Long companyId, Long courseId) {
        if (companyId == null || !companyId.equals(COMPANY_ID)) {
            ZhiQianTongPlusException.cast("你不能操作其他组织的课程信息，请稍后重试！");
        }
        LambdaQueryWrapper<CourseTeacher> lqw1 = new LambdaQueryWrapper<>();
        lqw1.eq(CourseTeacher::getCourseId,courseId);
        LambdaQueryWrapper<Teachplan> lqw2 = new LambdaQueryWrapper<>();
        lqw2.eq(Teachplan::getCourseId,courseId);
        LambdaQueryWrapper<CourseMarket> lqw3 = new LambdaQueryWrapper<>();
        lqw3.eq(CourseMarket::getId,courseId);
        LambdaQueryWrapper<CourseBase> lqw4 = new LambdaQueryWrapper<>();
        lqw4.eq(CourseBase::getId,courseId);
        LambdaQueryWrapper<TeachplanMedia> lqw5 = new LambdaQueryWrapper<>();
        lqw5.eq(TeachplanMedia::getCourseId,courseId);
        courseTeacherMapper.delete(lqw1);
        teachplanMapper.delete(lqw2);
        courseMarketMapper.delete(lqw3);
        int delete = courseBaseMapper.delete(lqw4);
        teachplanMediaMapper.delete(lqw5);
        if (delete <= 0) {
            ZhiQianTongPlusException.cast("删除课程信息出现了错误，请稍后重试！");
        }
    }

    //保存课程的营销信息
    private int saveCourseMarket(CourseMarket courseMarket) {
        String charge = courseMarket.getCharge();
        if (StringUtils.isBlank(charge)) {
            throw new ZhiQianTongPlusException("收费规则没有选择");
        }
        if (charge.equals(COURSE_IS_CHARGE)) {
            if (courseMarket.getPrice() == null || courseMarket.getPrice() <= 0) {
                throw new ZhiQianTongPlusException("课程为收费价格不能为空且必须大于0");
            }
        }
        CourseMarket courseMarketObj = courseMarketMapper.selectById(courseMarket.getId());
        if (courseMarketObj == null) {
            return courseMarketMapper.insert(courseMarket);
        }else {
            BeanUtils.copyProperties(courseMarket, courseMarketObj);
            courseMarketObj.setId(courseMarket.getId());
            return courseMarketMapper.updateById(courseMarketObj);
        }
    }

    private CourseBaseInfoDto getCourseBaseInfo(long courseId) {
        CourseBaseInfoDto courseBaseInfoDto = new CourseBaseInfoDto();
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if (courseBase == null) {
            return null;
        }
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        BeanUtils.copyProperties(courseBase,courseBaseInfoDto);
        if (courseMarket != null) {
            BeanUtils.copyProperties(courseMarket,courseBaseInfoDto);
        }

        CourseCategory courseCategoryBySt = courseCategoryMapper.selectById(courseBase.getSt());
        if (courseCategoryBySt == null) {
            courseCategoryBySt = new CourseCategory();
        }
        courseBaseInfoDto.setStName(courseCategoryBySt.getName());
        CourseCategory courseCategoryByMt = courseCategoryMapper.selectById(courseBase.getMt());
        if (courseCategoryByMt == null) {
            courseCategoryByMt = new CourseCategory();
        }
        courseBaseInfoDto.setMtName(courseCategoryByMt.getName());
        return courseBaseInfoDto;
    }

    private void courseBaseParamCheck(AddCourseDto params) {
        if (StringUtils.isBlank(params.getName())) {
            throw new ZhiQianTongPlusException("课程名称为空");
        }

        if (StringUtils.isBlank(params.getMt())) {
            throw new ZhiQianTongPlusException("课程分类为空");
        }

        if (StringUtils.isBlank(params.getSt())) {
            throw new ZhiQianTongPlusException("课程分类为空");
        }

        if (StringUtils.isBlank(params.getGrade())) {
            throw new ZhiQianTongPlusException("课程等级为空");
        }

        if (StringUtils.isBlank(params.getTeachmode())) {
            throw new ZhiQianTongPlusException("教育模式为空");
        }

        if (StringUtils.isBlank(params.getUsers())) {
            throw new ZhiQianTongPlusException("适应人群为空");
        }

        if (StringUtils.isBlank(params.getCharge())) {
            throw new ZhiQianTongPlusException("收费规则为空");
        }

    }

}
