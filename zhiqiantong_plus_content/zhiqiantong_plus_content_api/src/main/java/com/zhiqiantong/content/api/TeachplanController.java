package com.zhiqiantong.content.api;

import com.zhiqiantong.content.model.dto.BindTeachplanMediaDto;
import com.zhiqiantong.content.model.dto.SaveTeachplanDto;
import com.zhiqiantong.content.model.dto.TeachplanDto;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.web.bind.annotation.*;
import com.zhiqiantong.content.service.TeachplanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>
 * 课程计划 前端控制器
 * </p>
 *
 * @author zxg
 */
@Slf4j
@RestController
@RequestMapping("/teachplan")
@Api(value = "课程计划编辑接口",tags = "课程计划编辑接口")
public class TeachplanController {

    @Autowired
    private TeachplanService  teachplanService;

    @ApiOperation("查询课程计划树形结构")
    @ApiImplicitParam(value = "courseId",name = "课程Id",required = true,dataType = "Long",paramType = "path")
    @GetMapping("/{courseId}/tree-nodes")
    public List<TeachplanDto> getTreeNodes(@PathVariable Long courseId){
        return teachplanService.getTreeNodes(courseId);
    }

    @ApiOperation("课程计划创建或修改")
    @PostMapping("")
    public void saveTeachplan(@RequestBody SaveTeachplanDto teachplan){
        teachplanService.saveTeachplan(teachplan);
    }

    @ApiOperation("删除课程计划根据id")
    @DeleteMapping("/{teachplanId}")
    public void deletedTeachplanById(@PathVariable Long teachplanId){
        teachplanService.deletedTeachplanById(teachplanId);
    }

    @ApiOperation("上移课程计划根据id")
    @PostMapping("/moveup/{teachplanId}")
    public void moveUp(@PathVariable Long teachplanId){
        teachplanService.moveUp(teachplanId);
    }

    @ApiOperation("下移课程计划根据id")
    @PostMapping("/movedown/{teachplanId}")
    public void moveDown(@PathVariable Long teachplanId){
        teachplanService.moveDown(teachplanId);
    }

    @ApiOperation(value = "课程计划和媒资信息绑定")
    @PostMapping("/association/media")
    public void associationMedia(@RequestBody BindTeachplanMediaDto bindTeachplanMediaDto){
        teachplanService.associationMedia(bindTeachplanMediaDto);
    }

    @ApiOperation(value = "删除课程计划和媒资信息绑定的信息")
    @DeleteMapping("/association/media/{teachPlanId}/{mediaId}")
    public void deleteAssociationMedia(@PathVariable Long teachPlanId,@PathVariable String mediaId){
        teachplanService.deleteAssociationMedia(teachPlanId,mediaId);
    }
}
