package com.zhiqiantong.content.model.dto;

import com.zhiqiantong.content.model.po.Teachplan;
import com.zhiqiantong.content.model.po.TeachplanMedia;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @description 课程计划树型结构dto
 * @author Mr.M
 * @date 2022/9/9 10:27
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class TeachplanDto extends Teachplan {

  //课程计划关联的媒资信息
  TeachplanMedia teachplanMedia;

  //子结点
  List<TeachplanDto> teachPlanTreeNodes;

}
