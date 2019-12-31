package com.tensquare.recruit.service;

import com.tensquare.recruit.dao.EnterpriseDao;
import com.tensquare.recruit.pojo.Enterprise;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import util.IdWorker;


import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: tensquare_parent
 * @description:
 * @author: 好人
 * @date: 2019-12-22 15:45
 **/
@Service
@Transactional
public class EnterpreiseService {
    @Autowired
    private EnterpriseDao enterpriseDao;

    @Autowired
    private IdWorker idWorker;

    public List<Enterprise> hotList(String ishot){
        return enterpriseDao.findByIshot(ishot);
    }

    public List<Enterprise> findAll(){
        return enterpriseDao.findAll();
    }

    public Enterprise findById(String id){
        return enterpriseDao.findById(id).get();
    }

    public void save(Enterprise enterprise){
        enterprise.setId(idWorker.nextId()+"");
        enterpriseDao.save(enterprise);
    }

    public void update(Enterprise enterprise){
        enterpriseDao.save(enterprise);
    }

    public void deleteById(String id){
        enterpriseDao.deleteById(id);
    }

    /**
     * @Description: 条件查询
     * @param: label
     * @Return: java.util.List<com.tensquare.base.pojo.Label>
     * @Date: 2019-12-20 16:52
     */
    public List<Enterprise> findSearch(Enterprise enterprise) {
        return enterpriseDao.findAll(new Specification<Enterprise>() {
            @Override
            public Predicate toPredicate(Root<Enterprise> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if(!StringUtils.isBlank(enterprise.getName())){
                    list.add(criteriaBuilder.like(root.get("name").as(String.class),"%"+enterprise.getName()+"%"));
                }
                if(!StringUtils.isBlank(enterprise.getLabels())){
                    list.add(criteriaBuilder.equal(root.get("labels").as(String.class),enterprise.getLabels()));
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
    public Page<Enterprise> pageQuery(Enterprise enterprise, int page, int size) {
        //添加分页
        Pageable pageable = PageRequest.of(page-1,size);
        //添加条件
        return enterpriseDao.findAll(new Specification<Enterprise>() {
            @Override
            public Predicate toPredicate(Root<Enterprise> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if(!StringUtils.isBlank(enterprise.getName())){
                    list.add(criteriaBuilder.like(root.get("name").as(String.class),"%"+enterprise.getName()+"%"));
                }
                if(!StringUtils.isBlank(enterprise.getLabels())){
                    list.add(criteriaBuilder.equal(root.get("labels").as(String.class),enterprise.getLabels()));
                }

                Predicate[] predicates = new Predicate[list.size()];
                predicates = list.toArray(predicates);

                return criteriaBuilder.and(predicates);
            }
        },pageable);

    }
}
