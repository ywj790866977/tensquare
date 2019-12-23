package com.tensquare.recruit.controller;



import com.tensquare.recruit.pojo.Recruit;
import com.tensquare.recruit.service.RecruitService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Api(value = "/recruit",tags = "招聘模块")
@RestController
@CrossOrigin
@RequestMapping("/recruit")
public class RucruitController {

    @Autowired
    private RecruitService recruitService;

    @GetMapping("/search/recommend")
    @ApiOperation(value = "推荐职位",notes = "推荐职位")
    public Result recommend(){
        return new Result(true, StatusCode.OK,"查询成功",recruitService.recommend());
    }

    @GetMapping("/search/newlist")
    @ApiOperation(value = "最新职位",notes = "最新职位")
    public Result newList(){
        return new Result(true, StatusCode.OK,"查询成功",recruitService.newList());
    }

    @GetMapping
    @ApiOperation(value = "查询所有",notes = "查询所有")
    public Result findAll(){
        return new Result(true, StatusCode.OK,"查询成功",recruitService.findAll());
    }

    @GetMapping("/{recruitId}")
    @ApiOperation(value = "根据id查询",notes = "根据id查询")
    @ApiImplicitParam(name="recruitId", value="recruitId", required = true, dataType = "String")
    public Result findById(@PathVariable("recruitId") String recruitId){
        return new Result(true, StatusCode.OK,"查询成功",recruitService.findById(recruitId));
    }

    @PostMapping
    @ApiOperation(value = "添加招聘",notes = "添加招聘")
    public Result save(@RequestBody Recruit recruit){
        recruitService.save(recruit);
        return new Result(true, StatusCode.OK,"添加成功");
    }

    @PutMapping("/{recruitId}")
    @ApiOperation(value = "根据id修改",notes = "根据id修改")
    @ApiImplicitParam(name="recruitId", value="recruitId", required = true, dataType = "String")
    public Result updateById(@PathVariable("recruitId") String recruitId,@RequestBody Recruit recruit ){
        recruit.setId(recruitId);
        recruitService.update(recruit);
        return new Result(true, StatusCode.OK,"更新成功");
    }

    @DeleteMapping("/{recruitId}")
    @ApiOperation(value = "根据id删除",notes = "根据id删除")
    @ApiImplicitParam(name="recruitId", value="recruitId", required = true, dataType = "String")
    public Result deleteById(@PathVariable("recruitId") String recruitId){
        recruitService.deleteById(recruitId);
        return new Result(true, StatusCode.OK,"删除成功");
    }

    @PostMapping("/search")
    @ApiOperation(value = "条件查询",notes = "条件查询")
    public Result findSearch(@RequestBody Recruit recruit){
        return new Result(true, StatusCode.OK,"查询成功",recruitService.findSearch(recruit));
    }


    @PostMapping("/search/{page}/{size}")
    @ApiOperation(value = "分页查询",notes = "分页查询")
    public Result pageQuery(@RequestBody Recruit recruit,@PathVariable("page") int page,@PathVariable("size") int size){
        Page<Recruit> recruits = recruitService.pageQuery(recruit, page, size);
        PageResult<Recruit> pageResult = new PageResult<>(recruits.getTotalElements(),recruits.getContent());
        return new Result(true, StatusCode.OK,"查询成功",pageResult);
    }
}
