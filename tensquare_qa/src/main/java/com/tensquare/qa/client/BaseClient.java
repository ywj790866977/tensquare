package com.tensquare.qa.client;

import com.tensquare.qa.client.impl.BaseClientImpl;
import entity.Result;
import entity.StatusCode;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @program: tensquare_parent
 * @description:
 * @author: 好人
 * @date: 2019-12-27 19:40
 **/
@FeignClient(value = "tensquare-base",fallback = BaseClientImpl.class)
public interface BaseClient {

    @GetMapping("/label/{labelId}")
    public Result findById(@PathVariable("labelId") String labelId);
}