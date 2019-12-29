package com.tensquare.spit.service;

import com.tensquare.spit.dao.SpitDao;
import com.tensquare.spit.pojo.Spit;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;


import java.util.Date;
import java.util.List;

/**
 * @program: tensquare_parent
 * @description:
 * @author: 好人
 * @date: 2019-12-24 11:45
 **/
@Service
@Transactional
public class SpitService {

    @Autowired
    private SpitDao spitDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private MongoTemplate mongoTemplate;



    public Page<Spit> findByParentid(String parentId,int page, int size){
        Pageable pageable = PageRequest.of(page-1,size);
        return spitDao.findByParentid(parentId,pageable);
    }

    public List<Spit> findAll(){
        return spitDao.findAll();
    }

    public Spit findById(String id){
        return spitDao.findById(id).get();
    }

    public void save(Spit spit){
        spit.set_id(idWorker.nextId()+"");
        spit.setPublishtime(new Date()); // 发布日期
        spit.setVisits(0);  // 浏览量
        spit.setShare(0);   // 分项数
        spit.setThumbup(0); // 点赞数
        spit.setComment(0); // 回复数
        spit.setState("1"); // 状态
        //如果有父节点, 需要将父节点的回复数++
       if(!StringUtils.isBlank(spit.getParentid())){
           Query query = new Query();
           query.addCriteria(Criteria.where("_id").is(spit.getParentid()));
           Update update = new Update();
           update.inc("comment",1);
           mongoTemplate.updateFirst(query,update,"spit");
       }
        spitDao.save(spit);
    }


    public void update(Spit spit){
        spitDao.save(spit);
    }

    public void deleteById(String id){
        spitDao.deleteById(id);
    }

    public void thumbup(String spitId) {
//        Spit spit = spitDao.findById(spitId).get();
//        spit.setThumbup((spit.getThumbup() == null ? 0 : spit.getThumbup()) +1);
//        spitDao.save(spit);
        //方式二:
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(spitId));
        Update update = new Update();
        update.inc("thumbup",1);
        mongoTemplate.updateFirst(query,update,"spit");
    }
}
