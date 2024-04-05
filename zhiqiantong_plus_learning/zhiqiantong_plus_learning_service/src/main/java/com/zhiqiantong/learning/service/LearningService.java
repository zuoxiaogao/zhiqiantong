package com.zhiqiantong.learning.service;

import com.zhiqiantong.base.model.RestResponse;

/**
 * @author Mr.M
 * @version 1.0
 * @description 学习过程管理service接口
 * @date 2022/10/2 16:07
 */
public interface LearningService {

    /**
     * @param courseId    课程id
     * @param teachplanId 课程计划id
     * @param mediaId     视频文件id
     * @return com.xuecheng.base.model.RestResponse<java.lang.String>
     * @description 获取教学视频
     * @author Mr.M
     * @date 2022/10/5 9:08
     */
    RestResponse<String> getVideo(String userId, Long courseId, Long teachplanId, String mediaId);
}
