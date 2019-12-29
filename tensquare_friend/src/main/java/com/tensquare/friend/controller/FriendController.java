package com.tensquare.friend.controller;

import com.tensquare.friend.clien.UserClien;
import com.tensquare.friend.service.FriendService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * @program: tensquare_parent
 * @description:
 * @author: 好人
 * @date: 2019-12-28 12:30
 **/
@RestController
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private FriendService friendService;

    @Autowired
    private UserClien userClien;

    @PutMapping("/like/{friendId}/{type}")
    public Result addFriend(@PathVariable String friendId,@PathVariable String type){
        //判断是否登录
        Claims claims = (Claims) request.getAttribute("claims_user");
        if(claims == null){
            return new Result(false, StatusCode.LOGINERROR,"权限不足");
        }
        //判断类型
        if(type != null){
            //添加好友
            String userId = claims.getId();
            if("1".equals(type)){
                //喜欢
                int flag = friendService.addFriend(userId,friendId);
                if(flag == 0){
                    return new Result(false, StatusCode.LOGINERROR,"重复添加");
                }
                if(flag == 1){
                    userClien.updateFanscountAndFollowcount(userId,friendId,1);
                    return new Result(true,StatusCode.OK,"添加成功");
                }
            }else if("2".equals(type)){
                //不喜欢
                int flag = friendService.addNofriend(userId,friendId);
                if(flag == 0){
                    return new Result(false, StatusCode.LOGINERROR,"重复添加");
                }
                if(flag == 1){
                    return new Result(true,StatusCode.OK,"添加成功");
                }

            }else {
                return new Result(false, StatusCode.LOGINERROR,"参数异常");
            }
        }else{
            return new Result(false, StatusCode.LOGINERROR,"参数异常");
        }
        return new Result(true,StatusCode.OK,"添加成功");

    }


    @DeleteMapping("/{friendId}")
    public Result deleteFriend(@PathVariable("friendId") String friendId){
        //判断是否登录
        Claims claims = (Claims) request.getAttribute("claims_user");
        if(claims == null){
            return new Result(false, StatusCode.LOGINERROR,"权限不足");
        }
        //获得用户Id
        String userId = claims.getId();
        friendService.deleteFriend(userId,friendId);
        //修改关注数和粉丝数
        userClien.updateFanscountAndFollowcount(userId,friendId,-1);
        return new Result(true,StatusCode.OK,"删除成功");
    }
}
