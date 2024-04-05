package com.zhiqiantong.learning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhiqiantong.base.execption.ZhiQianTongPlusException;
import com.zhiqiantong.base.model.PageResult;
import com.zhiqiantong.content.model.po.CoursePublish;
import com.zhiqiantong.learning.feignclient.ContentServiceClient;
import com.zhiqiantong.learning.mapper.ZqtChooseCourseMapper;
import com.zhiqiantong.learning.mapper.ZqtCourseTablesMapper;
import com.zhiqiantong.learning.model.dto.MyCourseTableParams;
import com.zhiqiantong.learning.model.dto.ZqtChooseCourseDto;
import com.zhiqiantong.learning.model.dto.ZqtCourseTablesDto;
import com.zhiqiantong.learning.model.po.ZqtChooseCourse;
import com.zhiqiantong.learning.model.po.ZqtCourseTables;
import com.zhiqiantong.learning.service.MyCourseTablesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class MyCourseTablesServiceImpl implements MyCourseTablesService {
    @Autowired
    ZqtChooseCourseMapper zqtChooseCourseMapper;

    @Autowired
    ZqtCourseTablesMapper zqtCourseTablesMapper;

    @Autowired
    ContentServiceClient contentServiceClient;

    @Autowired
    MyCourseTablesService myCourseTablesService;


    @Transactional
    @Override
    public ZqtChooseCourseDto addChooseCourse(String userId, Long courseId) {
        //查询课程信息
        CoursePublish coursepublish = contentServiceClient.getCoursepublish(courseId);
        //课程收费标准
        String charge = coursepublish.getCharge();
        //选课记录
        ZqtChooseCourse chooseCourse = null;
        if("201000".equals(charge)){//课程免费
            //添加免费课程
            chooseCourse  = addFreeCourse(userId, coursepublish);
            if (chooseCourse == null) {
                ZhiQianTongPlusException.cast("添加到选课记录失败，请重试！");
            }
            //添加到我的课程表
            ZqtCourseTables zqtCourseTables = addCourseTable(chooseCourse);
            if (zqtCourseTables == null) {
                ZhiQianTongPlusException.cast("添加到课程表失败！");
            }

        }else{
            //添加收费课程
            chooseCourse  = addChargeCourse(userId, coursepublish);
            if (chooseCourse == null) {
                ZhiQianTongPlusException.cast("添加到选课记录失败，请重试！");
            }
        }
        ZqtChooseCourseDto zqtChooseCourseDto = new ZqtChooseCourseDto();
        BeanUtils.copyProperties(chooseCourse,zqtChooseCourseDto);
        //获取学习资格
        ZqtCourseTablesDto zqtCourseTablesDto = getLearningStatus(userId, courseId);
        zqtChooseCourseDto.setLearnStatus(zqtCourseTablesDto.getLearnStatus());
        return zqtChooseCourseDto;
    }

    /**
     * @description 判断学习资格
     * @param userId 用户id
     * @param courseId 课程id
     * @return ZqtCourseTablesDto 学习资格状态 [{"code":"702001","desc":"正常学习"},{"code":"702002","desc":"没有选课或选课后没有支付"},{"code":"702003","desc":"已过期需要申请续期或重新支付"}]
     * @author Mr.M
     * @date 2022/10/3 7:37
     */
    @Override
    public ZqtCourseTablesDto getLearningStatus(String userId, Long courseId) {
        //查询我的课程表
        ZqtCourseTables zqtCourseTables = getZqtCourseTables(userId, courseId);
        if(zqtCourseTables==null){
            ZqtCourseTablesDto zqtCourseTablesDto = new ZqtCourseTablesDto();
            //没有选课或选课后没有支付
            zqtCourseTablesDto.setLearnStatus("702002");
            return zqtCourseTablesDto;
        }
        ZqtCourseTablesDto zqtCourseTablesDto = new ZqtCourseTablesDto();
        BeanUtils.copyProperties(zqtCourseTables,zqtCourseTablesDto);
        //是否过期,true过期，false未过期
        boolean isExpires = zqtCourseTables.getValidtimeEnd().isBefore(LocalDateTime.now());
        if(!isExpires){
            //正常学习
            zqtCourseTablesDto.setLearnStatus("702001");

        }else{
            //已过期
            zqtCourseTablesDto.setLearnStatus("702003");
        }
        return zqtCourseTablesDto;

    }

    @Transactional
    @Override
    public boolean saveChooseCourseStauts(String choosecourseId) {
        ZqtChooseCourse zqtChooseCourse = zqtChooseCourseMapper.selectById(choosecourseId);
        if (zqtChooseCourse == null) {
            ZhiQianTongPlusException.cast("选课记录为空，请联系工作人员！");
        }
        zqtChooseCourse.setStatus("701001");
        int i = zqtChooseCourseMapper.updateById(zqtChooseCourse);
        if (i <= 0) {
            ZhiQianTongPlusException.cast("选课出现了问题，请联系工作人员！");
        }
        ZqtCourseTables zqtCourseTables = addCourseTable(zqtChooseCourse);
        return zqtCourseTables != null;
    }

    @Override
    public PageResult<ZqtCourseTables> mycourestabls(MyCourseTableParams params) {
        //页码
        long pageNo = params.getPage();
        //每页记录数,固定为4
        long pageSize = 4;
        //分页条件
        Page<ZqtCourseTables> page = new Page<>(pageNo, pageSize);
        //根据用户id查询
        String userId = params.getUserId();
        LambdaQueryWrapper<ZqtCourseTables> lambdaQueryWrapper = new LambdaQueryWrapper<ZqtCourseTables>().eq(ZqtCourseTables::getUserId, userId);

        //分页查询
        Page<ZqtCourseTables> pageResult = zqtCourseTablesMapper.selectPage(page, lambdaQueryWrapper);
        List<ZqtCourseTables> records = pageResult.getRecords();
        //记录总数
        long total = pageResult.getTotal();
        return new PageResult<>(records, total, pageNo, pageSize);

    }


    //添加免费课程,免费课程加入选课记录表、我的课程表
    public ZqtChooseCourse addFreeCourse(String userId, CoursePublish coursepublish) {

        //查询选课记录表是否存在免费的且选课成功的订单
        LambdaQueryWrapper<ZqtChooseCourse> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper = queryWrapper.eq(ZqtChooseCourse::getUserId, userId)
                .eq(ZqtChooseCourse::getCourseId, coursepublish.getId())
                .eq(ZqtChooseCourse::getOrderType, "700001")//免费课程
                .eq(ZqtChooseCourse::getStatus, "701001");//选课成功
        List<ZqtChooseCourse> zqtChooseCourses = zqtChooseCourseMapper.selectList(queryWrapper);
        if (zqtChooseCourses != null && zqtChooseCourses.size()>0) {
            return zqtChooseCourses.get(0);
        }
        //添加选课记录信息
        ZqtChooseCourse zqtChooseCourse = new ZqtChooseCourse();
        zqtChooseCourse.setCourseId(coursepublish.getId());
        zqtChooseCourse.setCourseName(coursepublish.getName());
        zqtChooseCourse.setCoursePrice(0f);//免费课程价格为0
        zqtChooseCourse.setUserId(userId);
        zqtChooseCourse.setCompanyId(coursepublish.getCompanyId());
        zqtChooseCourse.setOrderType("700001");//免费课程
        zqtChooseCourse.setCreateDate(LocalDateTime.now());
        zqtChooseCourse.setStatus("701001");//选课成功

        zqtChooseCourse.setValidDays(365);//免费课程默认365
        zqtChooseCourse.setValidtimeStart(LocalDateTime.now());
        zqtChooseCourse.setValidtimeEnd(LocalDateTime.now().plusDays(365));
        int insert = zqtChooseCourseMapper.insert(zqtChooseCourse);
        if (insert <= 0) {
            return null;
        }
        return zqtChooseCourse;

    }

    //添加收费课程
    public ZqtChooseCourse addChargeCourse(String userId, CoursePublish coursepublish){

        //如果存在待支付交易记录直接返回
        LambdaQueryWrapper<ZqtChooseCourse> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper = queryWrapper.eq(ZqtChooseCourse::getUserId, userId)
                .eq(ZqtChooseCourse::getCourseId, coursepublish.getId())
                .eq(ZqtChooseCourse::getOrderType, "700002")//收费订单
                .eq(ZqtChooseCourse::getStatus, "701002");//待支付
        List<ZqtChooseCourse> zqtChooseCourses = zqtChooseCourseMapper.selectList(queryWrapper);
        if (zqtChooseCourses != null && zqtChooseCourses.size()>0) {
            return zqtChooseCourses.get(0);
        }

        ZqtChooseCourse zqtChooseCourse = new ZqtChooseCourse();
        zqtChooseCourse.setCourseId(coursepublish.getId());
        zqtChooseCourse.setCourseName(coursepublish.getName());
        zqtChooseCourse.setCoursePrice(coursepublish.getPrice());
        zqtChooseCourse.setUserId(userId);
        zqtChooseCourse.setCompanyId(coursepublish.getCompanyId());
        zqtChooseCourse.setOrderType("700002");//收费课程
        zqtChooseCourse.setCreateDate(LocalDateTime.now());
        zqtChooseCourse.setStatus("701002");//待支付

        zqtChooseCourse.setValidDays(coursepublish.getValidDays());
        zqtChooseCourse.setValidtimeStart(LocalDateTime.now());
        zqtChooseCourse.setValidtimeEnd(LocalDateTime.now().plusDays(coursepublish.getValidDays()));
        int insert = zqtChooseCourseMapper.insert(zqtChooseCourse);
        if (insert <= 0) {
            return null;
        }
        return zqtChooseCourse;

    }
    //添加到我的课程表
    public ZqtCourseTables addCourseTable(ZqtChooseCourse zqtChooseCourse){
        //选课记录完成且未过期可以添加课程到课程表
        String status = zqtChooseCourse.getStatus();
        if (!"701001".equals(status)){
            ZhiQianTongPlusException.cast("选课未完成，无法添加到课程表");
        }
        //查询我的课程表
        ZqtCourseTables zqtCourseTables = getZqtCourseTables(zqtChooseCourse.getUserId(), zqtChooseCourse.getCourseId());
        if(zqtCourseTables!=null){
            return zqtCourseTables;
        }
        ZqtCourseTables zqtCourseTablesNew = new ZqtCourseTables();
        zqtCourseTablesNew.setChooseCourseId(zqtChooseCourse.getId());
        zqtCourseTablesNew.setUserId(zqtChooseCourse.getUserId());
        zqtCourseTablesNew.setCourseId(zqtChooseCourse.getCourseId());
        zqtCourseTablesNew.setCompanyId(zqtChooseCourse.getCompanyId());
        zqtCourseTablesNew.setCourseName(zqtChooseCourse.getCourseName());
        zqtCourseTablesNew.setCreateDate(LocalDateTime.now());
        zqtCourseTablesNew.setValidtimeStart(zqtChooseCourse.getValidtimeStart());
        zqtCourseTablesNew.setValidtimeEnd(zqtChooseCourse.getValidtimeEnd());
        zqtCourseTablesNew.setCourseType(zqtChooseCourse.getOrderType());
        int insert = zqtCourseTablesMapper.insert(zqtCourseTablesNew);
        if (insert <= 0) {
            return null;
        }
        return zqtCourseTablesNew;

    }

    /**
     * @description 根据课程和用户查询我的课程表中某一门课程
     * @param userId
     * @param courseId
     * @return com.xuecheng.learning.model.po.ZqtCourseTables
     * @author Mr.M
     * @date 2022/10/2 17:07
     */
    public ZqtCourseTables getZqtCourseTables(String userId,Long courseId){
        return zqtCourseTablesMapper.selectOne(new LambdaQueryWrapper<ZqtCourseTables>().eq(ZqtCourseTables::getUserId, userId).eq(ZqtCourseTables::getCourseId, courseId));
    }


}
