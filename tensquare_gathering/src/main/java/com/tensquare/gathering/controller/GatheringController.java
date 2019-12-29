package com.tensquare.gathering.controller;

import com.tensquare.gathering.pojo.Gathering;
import com.tensquare.gathering.service.GatheringService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Api(value = "/gathering",tags = "活动模块")
@RestController
@CrossOrigin
@RequestMapping("/gathering")
public class GatheringController {

    @Autowired
    private GatheringService gatheringService;

    @GetMapping
    @ApiOperation(value = "查询所有",notes = "查询所有")
    public Result findAll(){
        return new Result(true, StatusCode.OK,"查询成功",gatheringService.findAll());
    }

    @GetMapping("/{gatheringId}")
    @ApiOperation(value = "根据id查询",notes = "根据id查询")
    @ApiImplicitParam(name="gatheringId", value="gatheringId", required = true, dataType = "String")
    public Result findById(@PathVariable("gatheringId") String gatheringId){
        return new Result(true, StatusCode.OK,"查询成功",gatheringService.findById(gatheringId));
    }

    @PostMapping
    @ApiOperation(value = "添加",notes = "添加")
    public Result save(@RequestBody Gathering gathering){
        gatheringService.save(gathering);
        return new Result(true, StatusCode.OK,"添加成功");
    }

    @PutMapping("/{gatheringId}")
    @ApiOperation(value = "根据id修改",notes = "根据id修改")
    @ApiImplicitParam(name="gatheringId", value="gatheringId", required = true, dataType = "String")
    public Result updateById(@PathVariable("gatheringId") String gatheringId,@RequestBody Gathering gathering){
        gathering.setId(gatheringId);
        gatheringService.update(gathering);
        return new Result(true, StatusCode.OK,"更新成功");
    }

    @DeleteMapping("/{gatheringId}")
    @ApiOperation(value = "根据id删除",notes = "根据id删除")
    @ApiImplicitParam(name="gatheringId", value="gatheringId", required = true, dataType = "String")
    public Result deleteById(@PathVariable("gatheringId") String gatheringId){
        gatheringService.deleteById(gatheringId);
        return new Result(true, StatusCode.OK,"删除成功");
    }

    @PostMapping("/search")
    @ApiOperation(value = "条件查询",notes = "条件查询")
    public Result findSearch(@RequestBody Gathering gathering){
        return new Result(true, StatusCode.OK,"查询成功",gatheringService.findSearch(gathering));
    }


    @PostMapping("/search/{page}/{size}")
    @ApiOperation(value = "分页查询",notes = "分页查询")
    public Result pageQuery(@RequestBody Gathering gathering,@PathVariable("page") int page,@PathVariable("size") int size){
        Page<Gathering> gatherings = gatheringService.pageQuery(gathering, page, size);
        PageResult<Gathering> pageResult = new PageResult<>(gatherings.getTotalElements(),gatherings.getContent());
        return new Result(true, StatusCode.OK,"查询成功",pageResult);
    }
}
