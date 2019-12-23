package com.tensquare.article.controller;

import com.tensquare.article.pojo.Article;
import com.tensquare.article.service.ArticleService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Api(value = "/article",tags = "文章模块")
@RestController
@CrossOrigin
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PutMapping("/examine/{articleId}")
    @ApiOperation(value = "文章审核",notes = "文章审核")
    public Result examine(@PathVariable("articleId")String articleId){
        articleService.updateState(articleId);
        return new Result(true, StatusCode.OK,"审核成功");
    }

    @PutMapping("/thumbup/{articleId}")
    @ApiOperation(value = "点赞",notes = "点赞")
    public Result thumbup(@PathVariable("articleId")String articleId){
        articleService.addThumbup(articleId);
        return new Result(true, StatusCode.OK,"点赞成功");
    }

    @GetMapping
    @ApiOperation(value = "查询所有",notes = "查询所有")
    public Result findAll(){
        return new Result(true, StatusCode.OK,"查询成功",articleService.findAll());
    }

    @GetMapping("/{articleId}")
    @ApiOperation(value = "根据id查询",notes = "根据id查询")
    @ApiImplicitParam(name="articleId", value="articleId", required = true, dataType = "String")
    public Result findById(@PathVariable("articleId") String articleId){
        return new Result(true, StatusCode.OK,"查询成功",articleService.findById(articleId));
    }

    @PostMapping
    @ApiOperation(value = "添加",notes = "添加")
    public Result save(@RequestBody Article article){
        articleService.save(article);
        return new Result(true, StatusCode.OK,"添加成功");
    }

    @PutMapping("/{articleId}")
    @ApiOperation(value = "根据id修改",notes = "根据id修改")
    @ApiImplicitParam(name="articleId", value="articleId", required = true, dataType = "String")
    public Result updateById(@PathVariable("articleId") String articleId,@RequestBody Article article){
        article.setId(articleId);
        articleService.update(article);
        return new Result(true, StatusCode.OK,"更新成功");
    }

    @DeleteMapping("/{articleId}")
    @ApiOperation(value = "根据id删除",notes = "根据id删除")
    @ApiImplicitParam(name="articleId", value="articleId", required = true, dataType = "String")
    public Result deleteById(@PathVariable("articleId") String articleId){
        articleService.deleteById(articleId);
        return new Result(true, StatusCode.OK,"删除成功");
    }

    @PostMapping("/search")
    @ApiOperation(value = "条件查询",notes = "条件查询")
    public Result findSearch(@RequestBody Article article){
        return new Result(true, StatusCode.OK,"查询成功",articleService.findSearch(article));
    }


    @PostMapping("/search/{page}/{size}")
    @ApiOperation(value = "分页查询",notes = "分页查询")
    public Result pageQuery(@RequestBody Article article,@PathVariable("page") int page,@PathVariable("size") int size){
        Page<Article> articles = articleService.pageQuery(article, page, size);
        PageResult<Article> pageResult = new PageResult<>(articles.getTotalElements(),articles.getContent());
        return new Result(true, StatusCode.OK,"查询成功",pageResult);
    }
}
