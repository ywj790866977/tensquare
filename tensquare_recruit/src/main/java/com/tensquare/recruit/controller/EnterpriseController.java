package com.tensquare.recruit.controller;



import com.tensquare.recruit.pojo.Enterprise;
import com.tensquare.recruit.service.EnterpreiseService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Api(value = "/enterprise",tags = "企业模块")
@RestController
@CrossOrigin
@RequestMapping("/enterprise")
public class EnterpriseController {

    @Autowired
    private EnterpreiseService enterpreiseService;


    @GetMapping("/search/hotlist")
    public Result hotList(){
        return new Result(true, StatusCode.OK,"查询成功",enterpreiseService.hotList("1"));
    }

    @GetMapping
    @ApiOperation(value = "查询所有",notes = "查询所有")
    public Result findAll(){
        return new Result(true, StatusCode.OK,"查询成功",enterpreiseService.findAll());
    }

    @GetMapping("/{enterpriseId}")
    @ApiOperation(value = "根据id查询",notes = "根据id查询")
    @ApiImplicitParam(name="enterpriseId", value="enterpriseId", required = true, dataType = "String")
    public Result findById(@PathVariable("enterpriseId") String enterpriseId){
        return new Result(true, StatusCode.OK,"查询成功",enterpreiseService.findById(enterpriseId));
    }

    @PostMapping
    @ApiOperation(value = "添加新企业",notes = "添加新企业")
    public Result save(@RequestBody Enterprise enterprise){
        enterpreiseService.save(enterprise);
        return new Result(true, StatusCode.OK,"添加成功");
    }

    @PutMapping("/{enterpreiseId}")
    @ApiOperation(value = "根据id修改",notes = "根据id修改")
    @ApiImplicitParam(name="enterpriseId", value="enterpriseId", required = true, dataType = "String")
    public Result updateById(@PathVariable("enterpriseId") String enterpriseId,@RequestBody Enterprise enterprise){
        enterprise.setId(enterpriseId);
        enterpreiseService.update(enterprise);
        return new Result(true, StatusCode.OK,"更新成功");
    }

    @DeleteMapping("/{enterpreiseId}")
    @ApiOperation(value = "根据id删除",notes = "根据id删除")
    @ApiImplicitParam(name="enterpriseId", value="enterpriseId", required = true, dataType = "String")
    public Result deleteById(@PathVariable("enterpriseId") String enterpriseId){
        enterpreiseService.deleteById(enterpriseId);
        return new Result(true, StatusCode.OK,"删除成功");
    }

    @PostMapping("/search")
    @ApiOperation(value = "条件查询",notes = "条件查询")
    public Result findSearch(@RequestBody Enterprise enterprise){
        return new Result(true, StatusCode.OK,"查询成功",enterpreiseService.findSearch(enterprise));
    }


    @PostMapping("/search/{page}/{size}")
    @ApiOperation(value = "分页查询",notes = "分页查询")
    public Result pageQuery(@RequestBody Enterprise enterprise,@PathVariable("page") int page,@PathVariable("size") int size){
        Page<Enterprise> enterprises = enterpreiseService.pageQuery(enterprise, page, size);
        PageResult<Enterprise> pageResult = new PageResult<>(enterprises.getTotalElements(),enterprises.getContent());
        return new Result(true, StatusCode.OK,"查询成功",pageResult);
    }
}
