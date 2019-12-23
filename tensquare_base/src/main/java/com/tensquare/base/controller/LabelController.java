package com.tensquare.base.controller;

import com.tensquare.base.pojo.Label;
import com.tensquare.base.service.LabelService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import jdk.net.SocketFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "/label",tags = "标签模块")
@RestController
@CrossOrigin
@RequestMapping("/label")
public class LabelController {

    @Autowired
    private LabelService labelService;

    @GetMapping
    @ApiOperation(value = "查询所有",notes = "查询所有")
    public Result findAll(){
        return new Result(true, StatusCode.OK,"查询成功",labelService.findAll());
    }

    @GetMapping("/{labelId}")
    @ApiOperation(value = "根据id查询",notes = "根据id查询")
    @ApiImplicitParam(name="labelId", value="labelId", required = true, dataType = "String")
    public Result findById(@PathVariable("labelId") String labelId){
        return new Result(true, StatusCode.OK,"查询成功",labelService.findById(labelId));
    }

    @PostMapping
    @ApiOperation(value = "添加新标签",notes = "添加新标签")
    public Result save(@RequestBody Label label){
        labelService.save(label);
        return new Result(true, StatusCode.OK,"添加成功");
    }

    @PutMapping("/{labelId}")
    @ApiOperation(value = "根据id修改",notes = "根据id修改")
    @ApiImplicitParam(name="labelId", value="labelId", required = true, dataType = "String")
    public Result updateById(@PathVariable("labelId") String labelId,@RequestBody Label label){
        label.setId(labelId);
        labelService.update(label);
        return new Result(true, StatusCode.OK,"更新成功");
    }

    @DeleteMapping("/{labelId}")
    @ApiOperation(value = "根据id删除",notes = "根据id删除")
    @ApiImplicitParam(name="labelId", value="labelId", required = true, dataType = "String")
    public Result deleteById(@PathVariable("labelId") String labelId){
        labelService.deleteById(labelId);
        return new Result(true, StatusCode.OK,"删除成功");
    }

    @PostMapping("/search")
    @ApiOperation(value = "条件查询",notes = "条件查询")
    public Result findSearch(@RequestBody Label label){
        return new Result(true, StatusCode.OK,"查询成功",labelService.findSearch(label));
    }


    @PostMapping("/search/{page}/{size}")
    @ApiOperation(value = "分页查询",notes = "分页查询")
    public Result pageQuery(@RequestBody Label label,@PathVariable("page") int page,@PathVariable("size") int size){
        Page<Label> labels = labelService.pageQuery(label, page, size);
        PageResult<Label> pageResult = new PageResult<>(labels.getTotalElements(),labels.getContent());
        return new Result(true, StatusCode.OK,"查询成功",pageResult);
    }
}
