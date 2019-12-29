package com.tensquare.search.controller;

import com.tensquare.search.pojo.Article;
import com.tensquare.search.service.ArticleService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * @program: tensquare_parent
 * @description:
 * @author: 好人
 * @date: 2019-12-24 22:40
 **/
@RestController
@RequestMapping("/article")
@CrossOrigin
@Api(value = "/article",tags = "文章分词模块")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping()
    @ApiOperation(value = "文章分词保存",notes = "文章分词保存")
    public Result save(@RequestBody Article article){
        articleService.save(article);
        return new Result(true, StatusCode.OK,"保存成功");
    }

    @GetMapping("/{key}/{page}/{size}")
    @ApiOperation(value = "文章分词保存",notes = "文章分词保存")
    @ApiImplicitParam(name="key", value="key", required = true, dataType = "String")
    public Result findByKey(@PathVariable String key,@PathVariable int page,@PathVariable int size){
        Page<Article> pageData  = articleService.findByKey(key,page,size);
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<>(pageData.getTotalElements(),pageData.getContent()));
    }
}
