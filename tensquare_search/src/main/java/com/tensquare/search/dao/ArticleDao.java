package com.tensquare.search.dao;

import com.tensquare.search.pojo.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @program: tensquare_parent
 * @description:
 * @author: 好人
 * @date: 2019-12-24 22:29
 **/
public interface ArticleDao extends ElasticsearchRepository<Article,String> {
    Page<Article> findByTitleOrContentLike(String title, String content, Pageable pageable);
}
