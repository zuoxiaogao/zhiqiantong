package com.zhiqiantong.learning.service;

import com.zhiqiantong.base.model.PageResult;
import com.zhiqiantong.learning.model.dto.MyCourseTableParams;
import com.zhiqiantong.learning.model.dto.ZqtChooseCourseDto;
import com.zhiqiantong.learning.model.dto.ZqtCourseTablesDto;
import com.zhiqiantong.learning.model.po.ZqtCourseTables;

/**
 * @author Mr.M
 * @version 1.0
 * @description 我的课程表service接口
 * @date 2022/10/2 16:07
 */
public interface MyCourseTablesService {

    /**
     * @param userId   用户id
     * @param courseId 课程id
     * @return com.xuecheng.learning.model.dto.XcChooseCourseDto
     * @description 添加选课
     * @author Mr.M
     * @date 2022/10/24 17:33
     */
    ZqtChooseCourseDto addChooseCourse(String userId, Long courseId);

    /**
     * @param userId   用户id
     * @param courseId 课程id
     * @return XcCourseTablesDto 学习资格状态 [{"code":"702001","desc":"正常学习"},{"code":"702002","desc":"没有选课或选课后没有支付"},{"code":"702003","desc":"已过期需要申请续期或重新支付"}]
     * @description 判断学习资格
     * @author Mr.M
     * @date 2022/10/3 7:37
     */
    ZqtCourseTablesDto getLearningStatus(String userId, Long courseId);


    boolean saveChooseCourseStauts(String choosecourseId);

    /**
     * @param params
     * @return com.xuecheng.base.model.PageResult<com.xuecheng.learning.model.po.XcCourseTables>
     * @description 我的课程表
     * @author Mr.M
     * @date 2022/10/27 9:24
     */
    PageResult<ZqtCourseTables> mycourestabls(MyCourseTableParams params);

}
