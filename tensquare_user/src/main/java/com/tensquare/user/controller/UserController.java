package com.tensquare.user.controller;

import com.tensquare.user.pojo.User;
import com.tensquare.user.service.UserService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import util.JwtUtil;

import java.util.HashMap;
import java.util.Map;

@Api(value = "/user",tags = "用户模块")
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JwtUtil jwtUtil;


    @PutMapping("/{userid}/{friendid}/{x}")
    @ApiOperation(value = "更新粉丝和关注数",notes = "更新粉丝和关注数")
    public void updateFanscountAndFollowcount(@PathVariable("userid") String userid,@PathVariable("friendid") String friendid,@PathVariable("x") int x){
        userService.updateFanscountAndFollowcount(userid,friendid,x);
    }

    @PostMapping("/login")
    @ApiOperation(value = "登录",notes = "登录")
    public Result login(@RequestBody User user){
        User loginUser = userService.login(user.getMobile(),user.getPassword());
        if(loginUser == null){
            return new Result(false,StatusCode.LOGINERROR,"登录失败");
        }
        String token = jwtUtil.createJWT(loginUser.getId(), loginUser.getMobile(), "user");
        Map<String,String> map = new HashMap<>();
        map.put("token",token);
        map.put("roles","user");
        return new Result(true,StatusCode.OK,"登录成功",map);
    }

    @PostMapping("/regist/{code}")
    @ApiOperation(value = "注册",notes = "注册")
    public Result regist(@RequestBody User user,@PathVariable String code){
        //获取缓存中的验证码
        String redisCode = (String) redisTemplate.opsForValue().get("checkCode_" + user.getMobile());
        if(StringUtils.isEmpty(redisCode)){
            return new Result(false, StatusCode.ERROR,"请先获取验证码");
        }
        if(!redisCode.equals(code)){
            return new Result(false, StatusCode.ERROR,"请输入正确的验证码");
        }
        userService.save(user);
        return new Result(true, StatusCode.OK,"注册成功");
    }

    @PostMapping("/sendsms/{mobile}")
    @ApiOperation(value = "发送短信",notes = "发送短信")
    @ApiImplicitParam(name="mobile", value="mobile", required = true, dataType = "String")
    public Result sendSms(@PathVariable String mobile){
        userService.sendSms(mobile);
        return new Result(true, StatusCode.OK,"发送成功");
    }

    @GetMapping
    @ApiOperation(value = "查询所有",notes = "查询所有")
    public Result findAll(){
        return new Result(true, StatusCode.OK,"查询成功",userService.findAll());
    }

    @GetMapping("/{userId}")
    @ApiOperation(value = "根据id查询",notes = "根据id查询")
    @ApiImplicitParam(name="userId", value="userId", required = true, dataType = "String")
    public Result findById(@PathVariable("userId") String userId){
        return new Result(true, StatusCode.OK,"查询成功",userService.findById(userId));
    }

    @PostMapping
    @ApiOperation(value = "添加",notes = "添加")
    public Result save(@RequestBody User user){
        userService.save(user);
        return new Result(true, StatusCode.OK,"添加成功");
    }

    @PutMapping("/{userId}")
    @ApiOperation(value = "根据id修改",notes = "根据id修改")
    @ApiImplicitParam(name="userId", value="userId", required = true, dataType = "String")
    public Result updateById(@PathVariable("userId") String userId,@RequestBody User user){
        user.setId(userId);
        userService.update(user);
        return new Result(true, StatusCode.OK,"更新成功");
    }

    @DeleteMapping("/{userId}")
    @ApiOperation(value = "根据id删除",notes = "根据id删除")
    @ApiImplicitParam(name="userId", value="userId", required = true, dataType = "String")
    public Result deleteById(@PathVariable("userId") String userId){
        userService.deleteById(userId);
        return new Result(true, StatusCode.OK,"删除成功");
    }

    @PostMapping("/search")
    @ApiOperation(value = "条件查询",notes = "条件查询")
    public Result findSearch(@RequestBody User user){
        return new Result(true, StatusCode.OK,"查询成功",userService.findSearch(user));
    }


    @PostMapping("/search/{page}/{size}")
    @ApiOperation(value = "分页查询",notes = "分页查询")
    public Result pageQuery(@RequestBody User user,@PathVariable("page") int page,@PathVariable("size") int size){
        Page<User> users = userService.pageQuery(user, page, size);
        PageResult<User> pageResult = new PageResult<>(users.getTotalElements(),users.getContent());
        return new Result(true, StatusCode.OK,"查询成功",pageResult);
    }
}
