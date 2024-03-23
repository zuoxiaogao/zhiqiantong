package com.zhiqiantong.content.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;

/**
 * @description 编辑课程dto
 * @author Mr.M
 * @date 2022/9/7 17:40
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value="EditCourseDto", description="修改课程基本信息")
public class EditCourseDto extends AddCourseDto {

 @NotEmpty(message = "课程id为空，请退出重试！")
 @ApiModelProperty(value = "课程id", required = true)
 private Long id;

}
