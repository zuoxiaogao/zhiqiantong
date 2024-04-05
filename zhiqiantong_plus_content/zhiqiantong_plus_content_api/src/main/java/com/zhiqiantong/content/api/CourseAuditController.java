package com.zhiqiantong.content.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zhiqiantong.content.service.CourseAuditService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zxg
 */
@Slf4j
@RestController
@RequestMapping("courseAudit")
public class CourseAuditController {

    @Autowired
    private CourseAuditService  courseAuditService;
}
