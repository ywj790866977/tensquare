package com.tensquare.article.service;

import com.tensquare.article.dao.ArticleDao;
import com.tensquare.article.pojo.Article;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
@Transactional
public class ArticleService {
    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisTemplate redisTemplate;

    public void updateState(String id ){
        articleDao.updateState(id);
    }

    public void addThumbup(String id){
        articleDao.addThumbup(id);
    }


    public List<Article> findAll(){
        return articleDao.findAll();
    }

    public Article findById(String id){
        //先从缓存中获取
        Article article =  (Article) redisTemplate.opsForValue().get("article_"+id);
        if(article==null){
            //从数据库中获取
            article = articleDao.findById(id).get();
            //放入redis
            redisTemplate.opsForValue().set("article_"+id,article,10, TimeUnit.MINUTES);
        }
        return article;
    }

    public void save(Article article){
        article.setId(idWorker.nextId()+"");
        articleDao.save(article);
    }

    public void update(Article article){
        //先删除缓存
        redisTemplate.delete("article_"+article.getId());
        articleDao.save(article);
    }

    public void deleteById(String id){
        //删除缓存
        redisTemplate.delete("article_"+id);
        articleDao.deleteById(id);
    }

    /**
     * @Description: 条件查询
     * @param: label
     * @Return: java.util.List<com.tensquare.base.pojo.Label>
     * @Date: 2019-12-20 16:52
     */
    public List<Article> findSearch(Article article) {
        return articleDao.findAll(new Specification<Article>() {
            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if(!StringUtils.isBlank(article.getTitle())){
                    list.add(criteriaBuilder.like(root.get("title").as(String.class),"%"+article.getTitle()+"%"));
                }
                if(!StringUtils.isBlank(article.getContent())){
                    list.add(criteriaBuilder.equal(root.get("content").as(String.class),article.getContent()));
                }

                Predicate[] predicates = new Predicate[list.size()];
                predicates = list.toArray(predicates);

                return criteriaBuilder.and(predicates);
            }
        });
    }

    /**
     * @Description: 分页查询
     * @param: label
     * @param: page
     * @param: size
     * @Return: org.springframework.data.domain.Page<com.tensquare.base.pojo.Label>
     * @Date: 2019-12-20 17:12
     */
    public Page<Article> pageQuery(Article article, int page, int size) {
        //添加分页
        Pageable pageable = PageRequest.of(page-1,size);
        //添加条件
        return articleDao.findAll(new Specification<Article>() {
            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if(!StringUtils.isBlank(article.getTitle())){
                    list.add(criteriaBuilder.like(root.get("title").as(String.class),"%"+article.getTitle()+"%"));
                }
                if(!StringUtils.isBlank(article.getContent())){
                    list.add(criteriaBuilder.equal(root.get("content").as(String.class),article.getContent()));
                }

                Predicate[] predicates = new Predicate[list.size()];
                predicates = list.toArray(predicates);

                return criteriaBuilder.and(predicates);
            }
        },pageable);

    }
}
