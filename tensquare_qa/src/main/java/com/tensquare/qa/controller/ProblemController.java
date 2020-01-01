package com.tensquare.qa.controller;


import com.tensquare.qa.client.BaseClient;
import com.tensquare.qa.pojo.Problem;
import com.tensquare.qa.service.ProblemService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(value = "/problem",tags = "问答模块")
@RestController
@CrossOrigin
@RequestMapping("/problem")
public class ProblemController {

    @Autowired
    private ProblemService problemService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private BaseClient baseClient;

    @PutMapping("/review/{problemId}")
    public Result review(@PathVariable String problemId){
        Claims claims = (Claims) request.getAttribute("claims_admin");
        if(claims == null){
            return new Result(false, StatusCode.ACCESSERROR,"权限不足");
        }
        problemService.review(problemId);
        return new Result(true,StatusCode.OK,"审核成功");
    }


    @GetMapping("/userlist")
    @ApiOperation(value = "用户问题",notes = "用户问题")
    public Result userList(){
        Claims claims = (Claims) request.getAttribute("claims_user");
        if(claims == null){
            return new Result(false, StatusCode.ACCESSERROR,"权限不足");
        }
        String userid = claims.getId();
        List<Problem> list=  problemService.userlist(userid);
        return new Result(true,StatusCode.OK,"查询成功",list);
    }


    @PutMapping("/top/{problemId}")
    @ApiOperation(value = "顶帖",notes = "顶帖")
    public Result topPost(@PathVariable String problemId){
        int flag = problemService.topPost(problemId);
        if(flag == 0){
            return new Result(false,StatusCode.ERROR,"顶帖失败");
        }
        return new Result(true,StatusCode.OK,"顶帖成功");
    }

    @GetMapping("/label/{labelId}")
    public Result findByLabelid(@PathVariable("labelId") String labelId){
        return baseClient.findById(labelId);
    }



    @PostMapping("/newlist/{labelId}/{page}/{size}")
    @ApiOperation(value = "查询最新问答",notes = "查询最新问答")
    public Result newlist(@PathVariable("labelId") String labelId, @PathVariable("page") int page,@PathVariable("size") int size){
        Page<Problem> problems = problemService.newlist(labelId, page, size);
        PageResult<Problem> pageResult = new PageResult<>(problems.getTotalElements(),problems.getContent());
        return new Result(true, StatusCode.OK,"查询成功",pageResult);
    }

    @PostMapping("/hotlist/{labelId}/{page}/{size}")
    @ApiOperation(value = "查询热门问答",notes = "查询热门问答")
    public Result hotlist(@PathVariable("labelId") String labelId, @PathVariable("page") int page,@PathVariable("size") int size){
        Page<Problem> problems = problemService.hotlist(labelId, page, size);
        PageResult<Problem> pageResult = new PageResult<>(problems.getTotalElements(),problems.getContent());
        return new Result(true, StatusCode.OK,"查询成功",pageResult);
    }

    @PostMapping("/waitlist/{labelId}/{page}/{size}")
    @ApiOperation(value = "查询等待回答",notes = "查询等待回答")
    public Result watilist(@PathVariable("labelId") String labelId, @PathVariable("page") int page,@PathVariable("size") int size){
        Page<Problem> problems = problemService.waitlist(labelId, page, size);
        PageResult<Problem> pageResult = new PageResult<>(problems.getTotalElements(),problems.getContent());
        return new Result(true, StatusCode.OK,"查询成功",pageResult);
    }


    @GetMapping
    @ApiOperation(value = "查询所有",notes = "查询所有")
    public Result findAll(){
        return new Result(true, StatusCode.OK,"查询成功",problemService.findAll());
    }

    @GetMapping("/{problemId}")
    @ApiOperation(value = "根据id查询",notes = "根据id查询")
    @ApiImplicitParam(name="problemId", value="problemId", required = true, dataType = "String")
    public Result findById(@PathVariable("problemId") String problemId){
        return new Result(true, StatusCode.OK,"查询成功",problemService.findById(problemId));
    }

    @PostMapping
    @ApiOperation(value = "添加",notes = "添加")
    public Result save(@RequestBody Problem problem){
        Claims claims = (Claims) request.getAttribute("claims_user");
        if(claims == null){
            return new Result(false, StatusCode.ACCESSERROR,"权限不足");
        }
        problemService.save(problem);
        return new Result(true, StatusCode.OK,"添加成功");
    }

    @PutMapping("/{problemId}")
    @ApiOperation(value = "根据id修改",notes = "根据id修改")
    @ApiImplicitParam(name="problemId", value="problemId", required = true, dataType = "String")
    public Result updateById(@PathVariable("problemId") String problemId,@RequestBody Problem problem){
        problem.setId(problemId);
        problemService.update(problem);
        return new Result(true, StatusCode.OK,"更新成功");
    }

    @DeleteMapping("/{problemId}")
    @ApiOperation(value = "根据id删除",notes = "根据id删除")
    @ApiImplicitParam(name="problemId", value="problemId", required = true, dataType = "String")
    public Result deleteById(@PathVariable("problemId") String problemId){
        problemService.deleteById(problemId);
        return new Result(true, StatusCode.OK,"删除成功");
    }

    @PostMapping("/search")
    @ApiOperation(value = "条件查询",notes = "条件查询")
    public Result findSearch(@RequestBody Problem problem){
        return new Result(true, StatusCode.OK,"查询成功",problemService.findSearch(problem));
    }


    @PostMapping("/search/{page}/{size}")
    @ApiOperation(value = "分页查询",notes = "分页查询")
    public Result pageQuery(@RequestBody Problem problem,@PathVariable("page") int page,@PathVariable("size") int size){
        Page<Problem> problems = problemService.pageQuery(problem, page, size);
        PageResult<Problem> pageResult = new PageResult<>(problems.getTotalElements(),problems.getContent());
        return new Result(true, StatusCode.OK,"查询成功",pageResult);
    }
}
