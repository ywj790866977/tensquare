package com.tensquare.spit.controller;

import com.tensquare.spit.pojo.Spit;
import com.tensquare.spit.service.SpitService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * @program: tensquare_parent
 * @description:
 * @author: 好人
 * @date: 2019-12-24 11:51
 **/
@RestController
@RequestMapping("/spit")
@CrossOrigin
@Api(value = "/spit",tags = "吐槽模块")
public class SpitController {

    @Autowired
    private SpitService spitService;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/comment/{parentid}/{page}/{size}")
    @ApiOperation(value = "根据parenid查询",notes = "根据parenid查询")
    public Result findByParentid(@PathVariable("parentid") String parentid,@PathVariable("page") int page,@PathVariable("size")int size){
        Page<Spit> spitPage = spitService.findByParentid(parentid, page, size);
        return new Result(true, StatusCode.OK,"查询成功",new PageResult<Spit>(spitPage.getTotalElements(),spitPage.getContent()));
    }

    @PutMapping("/thumbup/{spitId}")
    @ApiOperation(value = "点赞",notes = "点赞")
    public Result thumbup(@PathVariable("spitId")String spitId){
        //判断当前用户是否点赞,因为没做认证,先把用户id写死
        String userid = "111";
        if(redisTemplate.opsForValue().get("thumbup_"+userid) != null) {
            return new Result(false,StatusCode.REPERROR,"重复操作");
        }
        spitService.thumbup(spitId);
        redisTemplate.opsForValue().set("thumbup_"+userid,1);
        return new Result(true,StatusCode.OK,"点赞成功");
    }

    @GetMapping
    @ApiOperation(value = "查询所有",notes = "查询所有")
    public Result findAll(){
        return new Result(true, StatusCode.OK,"查询成功",spitService.findAll());
    }

    @GetMapping("/{spitId}")
    @ApiOperation(value = "根据id查询",notes = "根据id查询")
    @ApiImplicitParam(name="spitId", value="spitId", required = true, dataType = "String")
    public Result findById(@PathVariable("spitId") String spitId){
        return new Result(true, StatusCode.OK,"查询成功",spitService.findById(spitId));
    }

    @PostMapping
    @ApiOperation(value = "添加新标签",notes = "添加新标签")
    public Result save(@RequestBody Spit spit){
        spitService.save(spit);
        return new Result(true, StatusCode.OK,"添加成功");
    }

    @PutMapping("/{spitId}")
    @ApiOperation(value = "根据id修改",notes = "根据id修改")
    @ApiImplicitParam(name="spitId", value="spitId", required = true, dataType = "String")
    public Result updateById(@PathVariable("spitId") String spitId,@RequestBody Spit spit){
        spit.set_id(spitId);
        spitService.update(spit);
        return new Result(true, StatusCode.OK,"更新成功");
    }

    @DeleteMapping("/{spitId}")
    @ApiOperation(value = "根据id删除",notes = "根据id删除")
    @ApiImplicitParam(name="spitId", value="spitId", required = true, dataType = "String")
    public Result deleteById(@PathVariable("spitId") String spitId){
        spitService.deleteById(spitId);
        return new Result(true, StatusCode.OK,"删除成功");
    }
}
