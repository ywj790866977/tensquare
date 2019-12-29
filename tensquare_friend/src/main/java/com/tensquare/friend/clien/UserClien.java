package com.tensquare.friend.clien;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * @program: tensquare_parent
 * @description:
 * @author: 好人
 * @date: 2019-12-28 22:55
 **/
@FeignClient("tensquare-user")
public interface UserClien {
    @PutMapping("/user/{userid}/{friendid}/{x}")
    public void updateFanscountAndFollowcount(@PathVariable("userid") String userid, @PathVariable("friendid") String friendid, @PathVariable("x") int x);
}
