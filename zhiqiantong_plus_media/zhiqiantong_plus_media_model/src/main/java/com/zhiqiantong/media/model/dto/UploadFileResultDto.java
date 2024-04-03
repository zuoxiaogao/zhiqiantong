package com.zhiqiantong.media.model.dto;

import com.zhiqiantong.media.model.po.MediaFiles;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Mr.M
 * @version 1.0
 * @description 上传普通文件成功响应结果
 * @date 2022/9/12 18:49
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UploadFileResultDto extends MediaFiles {
    String timelength;
}
