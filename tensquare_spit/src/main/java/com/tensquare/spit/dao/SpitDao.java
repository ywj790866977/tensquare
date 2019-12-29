package com.tensquare.spit.dao;

import com.tensquare.spit.pojo.Spit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import sun.security.provider.ConfigFile;

/**
 * @program: tensquare_parent
 * @description:
 * @author: 好人
 * @date: 2019-12-24 11:45
 **/
public interface SpitDao extends MongoRepository<Spit,String> {
    /**
     * @Description: 根据上级id查询子列表
     * @param: parentId
     * @param: pageable
     * @Return: org.springframework.data.domain.Page<com.tensquare.spit.pojo.Spit>
     * @Date: 2019-12-24 14:32
     */
    public Page<Spit> findByParentid(String parentId, Pageable pageable);

}
