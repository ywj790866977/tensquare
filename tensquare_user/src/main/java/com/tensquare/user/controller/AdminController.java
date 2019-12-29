package com.tensquare.user.controller;

import com.tensquare.user.pojo.Admin;
import com.tensquare.user.service.AdminService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import util.JwtUtil;

import java.util.HashMap;
import java.util.Map;

@Api(value = "/admin",tags = "管理员模块")
@RestController
@CrossOrigin
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    @ApiOperation(value = "登录",notes = "登录")
    public Result login(@RequestBody Admin admin){
        Admin adminLogin =   adminService.login(admin);
        if(adminLogin == null){
            return new Result(false, StatusCode.LOGINERROR,"登录失败");
        }
        String token = jwtUtil.createJWT(adminLogin.getId(), adminLogin.getLoginname(), "admin");
        Map<String,Object> map = new HashMap<>();
        map.put("token",token);
        map.put("role","admin");
        return new Result(true, StatusCode.OK,"登录成功",map);
    }

    @GetMapping
    @ApiOperation(value = "查询所有",notes = "查询所有")
    public Result findAll(){
        return new Result(true, StatusCode.OK,"查询成功",adminService.findAll());
    }

    @GetMapping("/{adminId}")
    @ApiOperation(value = "根据id查询",notes = "根据id查询")
    @ApiImplicitParam(name="adminId", value="adminId", required = true, dataType = "String")
    public Result findById(@PathVariable("adminId") String adminId){
        return new Result(true, StatusCode.OK,"查询成功",adminService.findById(adminId));
    }

    @PostMapping
    @ApiOperation(value = "添加",notes = "添加")
    public Result save(@RequestBody Admin admin){
        adminService.save(admin);
        return new Result(true, StatusCode.OK,"添加成功");
    }

    @PutMapping("/{adminId}")
    @ApiOperation(value = "根据id修改",notes = "根据id修改")
    @ApiImplicitParam(name="adminId", value="adminId", required = true, dataType = "String")
    public Result updateById(@PathVariable("adminId") String adminId,@RequestBody Admin admin){
        admin.setId(adminId);
        adminService.update(admin);
        return new Result(true, StatusCode.OK,"更新成功");
    }

    @DeleteMapping("/{adminId}")
    @ApiOperation(value = "根据id删除",notes = "根据id删除")
    @ApiImplicitParam(name="adminId", value="adminId", required = true, dataType = "String")
    public Result deleteById(@PathVariable("adminId") String adminId){
        adminService.deleteById(adminId);
        return new Result(true, StatusCode.OK,"删除成功");
    }

    @PostMapping("/search")
    @ApiOperation(value = "条件查询",notes = "条件查询")
    public Result findSearch(@RequestBody Admin admin){
        return new Result(true, StatusCode.OK,"查询成功",adminService.findSearch(admin));
    }


    @PostMapping("/search/{page}/{size}")
    @ApiOperation(value = "分页查询",notes = "分页查询")
    public Result pageQuery(@RequestBody Admin admin,@PathVariable("page") int page,@PathVariable("size") int size){
        Page<Admin> admins = adminService.pageQuery(admin, page, size);
        PageResult<Admin> pageResult = new PageResult<>(admins.getTotalElements(),admins.getContent());
        return new Result(true, StatusCode.OK,"查询成功",pageResult);
    }
}
