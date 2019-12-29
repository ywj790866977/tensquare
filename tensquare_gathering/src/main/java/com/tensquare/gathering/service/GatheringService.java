package com.tensquare.gathering.service;

import com.tensquare.gathering.dao.GatheringDao;
import com.tensquare.gathering.pojo.Gathering;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class GatheringService {
    @Autowired
    private GatheringDao gatheringDao;

    @Autowired
    private IdWorker idWorker;

    public List<Gathering> findAll(){
        return gatheringDao.findAll();
    }

    @Cacheable(value = "gathering",key = "#id")
    public Gathering findById(String id){
        return gatheringDao.findById(id).get();
    }

    public void save(Gathering gathering){
        gathering.setId(idWorker.nextId()+"");
        gatheringDao.save(gathering);
    }

    @CacheEvict(value = "gathering",key = "#gathering.id")
    public void update(Gathering gathering){
        gatheringDao.save(gathering);
    }

    @CacheEvict(value = "gathering",key = "#id")
    public void deleteById(String id){
        gatheringDao.deleteById(id);
    }

    /**
     * @Description: 条件查询
     * @param: label
     * @Return: java.util.List<com.tensquare.base.pojo.Gathering>
     * @Date: 2019-12-20 16:52
     */
    public List<Gathering> findSearch(Gathering gathering) {
        return gatheringDao.findAll(new Specification<Gathering>() {
            @Override
            public Predicate toPredicate(Root<Gathering> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if(!StringUtils.isBlank(gathering.getName())){
                    list.add(criteriaBuilder.like(root.get("name").as(String.class),"%"+gathering.getName()+"%"));
                }
                if(!StringUtils.isBlank(gathering.getDetail())){
                    list.add(criteriaBuilder.equal(root.get("detail").as(String.class),gathering.getDetail()));
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
     * @Return: org.springframework.data.domain.Page<com.tensquare.base.pojo.Gathering>
     * @Date: 2019-12-20 17:12
     */
    public Page<Gathering> pageQuery(Gathering gathering, int page, int size) {
        //添加分页
        Pageable pageable = PageRequest.of(page-1,size);
        //添加条件
        return gatheringDao.findAll(new Specification<Gathering>() {
            @Override
            public Predicate toPredicate(Root<Gathering> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if(!StringUtils.isBlank(gathering.getName())){
                    list.add(criteriaBuilder.like(root.get("name").as(String.class),"%"+gathering.getName()+"%"));
                }
                if(!StringUtils.isBlank(gathering.getDetail())){
                    list.add(criteriaBuilder.equal(root.get("detail").as(String.class),gathering.getDetail()));
                }

                Predicate[] predicates = new Predicate[list.size()];
                predicates = list.toArray(predicates);

                return criteriaBuilder.and(predicates);
            }
        },pageable);

    }
}
