package com.zhiqiantong.learning.service.impl;

import com.zhiqiantong.base.execption.ZhiQianTongPlusException;
import com.zhiqiantong.base.model.RestResponse;
import com.zhiqiantong.content.model.po.CoursePublish;
import com.zhiqiantong.learning.feignclient.ContentServiceClient;
import com.zhiqiantong.learning.feignclient.MediaServiceClient;
import com.zhiqiantong.learning.model.dto.ZqtCourseTablesDto;
import com.zhiqiantong.learning.service.LearningService;
import com.zhiqiantong.learning.service.MyCourseTablesService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LearningServiceImpl implements LearningService {
    @Autowired
    ContentServiceClient contentServiceClient;
    @Autowired
    MediaServiceClient mediaServiceClient;
    @Autowired
    MyCourseTablesService myCourseTablesService;


    @Override
    public RestResponse<String> getVideo(String userId, Long courseId, Long teachplanId, String mediaId) {
        //查询课程信息
        CoursePublish coursepublish = contentServiceClient.getCoursepublish(courseId);
        if(coursepublish==null){
            ZhiQianTongPlusException.cast("课程信息不存在");
        }
        //校验学习资格

        //如果登录
        if(StringUtils.isNotEmpty(userId)){

            //判断是否选课，根据选课情况判断学习资格
            ZqtCourseTablesDto zqtCourseTablesDto = myCourseTablesService.getLearningStatus(userId, courseId);
            //学习资格状态 [{"code":"702001","desc":"正常学习"},{"code":"702002","desc":"没有选课或选课后没有支付"},{"code":"702003","desc":"已过期需要申请续期或重新支付"}]
            String learnStatus = zqtCourseTablesDto.getLearnStatus();
            if(learnStatus.equals("702001")){
                return mediaServiceClient.getPlayUrlByMediaId(mediaId);
            }else if(learnStatus.equals("702003")){
                RestResponse.validFail("您的选课已过期需要申请续期或重新支付");
            }
        }

        //未登录或未选课判断是否收费
        String charge = coursepublish.getCharge();
        if(charge.equals("201000")){//免费可以正常学习
            return mediaServiceClient.getPlayUrlByMediaId(mediaId);
        }

        return RestResponse.validFail("请购买课程后继续学习");

    }
}
