package com.zhiqiantong.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhiqiantong.content.model.dto.BindTeachplanMediaDto;
import com.zhiqiantong.content.model.dto.SaveTeachplanDto;
import com.zhiqiantong.content.model.dto.TeachplanDto;
import com.zhiqiantong.content.model.po.Teachplan;
import com.zhiqiantong.content.model.po.TeachplanMedia;

import java.util.List;

/**
 * <p>
 * 课程计划 服务类
 * </p>
 *
 * @author zxg
 * @since 2024-03-20
 */
public interface TeachplanService extends IService<Teachplan> {

    List<TeachplanDto> getTreeNodes(Long courseId);

    void saveTeachplan(SaveTeachplanDto teachplan);

    void deletedTeachplanById(Long teachplanId);

    void moveUp(Long teachplanId);

    void moveDown(Long teachplanId);

    /**
     * @param bindTeachplanMediaDto
     * @return com.xuecheng.content.model.po.TeachplanMedia
     * @description 教学计划绑定媒资
     * @author Mr.M
     * @date 2022/9/14 22:20
     */
    TeachplanMedia associationMedia(BindTeachplanMediaDto bindTeachplanMediaDto);

    void deleteAssociationMedia(Long teachPlanId, String mediaId);
}
