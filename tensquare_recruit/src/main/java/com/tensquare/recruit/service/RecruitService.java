package com.tensquare.recruit.service;

import com.tensquare.recruit.dao.RecruitDao;

import com.tensquare.recruit.pojo.Recruit;
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
public class RecruitService {
    @Autowired
    private RecruitDao recruitDao;

    @Autowired
    private IdWorker idWorker;

    public List<Recruit> recommend(){
        return recruitDao.findTop6ByStateOrderByCreatetimeDesc("2");
    }

    public List<Recruit> newList(){
        return recruitDao.findTop6ByStateNotOrderByCreatetimeDesc("0");
    }

    public List<Recruit> findAll(){
        return recruitDao.findAll();
    }

    public Recruit findById(String id){
        return recruitDao.findById(id).get();
    }

    public void save(Recruit recruit){
        recruit.setId(idWorker.nextId()+"");
        recruitDao.save(recruit);
    }

    public void update(Recruit recruit){
        recruitDao.save(recruit);
    }

    public void deleteById(String id){
        recruitDao.deleteById(id);
    }

    /**
     * @Description: 条件查询
     * @param: label
     * @Return: java.util.List<com.tensquare.base.pojo.Label>
     * @Date: 2019-12-20 16:52
     */
    public List<Recruit> findSearch(Recruit recruit) {
        return recruitDao.findAll(new Specification<Recruit>() {
            @Override
            public Predicate toPredicate(Root<Recruit> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if(!StringUtils.isBlank(recruit.getJobname())){
                    list.add(criteriaBuilder.like(root.get("jobname").as(String.class),"%"+recruit.getJobname()+"%"));
                }
                if(!StringUtils.isBlank(recruit.getSalary())){
                    list.add(criteriaBuilder.equal(root.get("salary").as(String.class),recruit.getSalary()));
                }
                if(!StringUtils.isBlank(recruit.getCondition())){
                    list.add(criteriaBuilder.equal(root.get("condition").as(String.class),recruit.getCondition()));
                }
                if(!StringUtils.isBlank(recruit.getEducation())){
                    list.add(criteriaBuilder.equal(root.get("education").as(String.class),recruit.getEducation()));
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
    public Page<Recruit> pageQuery(Recruit recruit, int page, int size) {
        //添加分页
        Pageable pageable = PageRequest.of(page-1,size);
        //添加条件
        return recruitDao.findAll(new Specification<Recruit>() {
            @Override
            public Predicate toPredicate(Root<Recruit> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if(!StringUtils.isBlank(recruit.getJobname())){
                    list.add(criteriaBuilder.like(root.get("jobname").as(String.class),"%"+recruit.getJobname()+"%"));
                }
                if(!StringUtils.isBlank(recruit.getSalary())){
                    list.add(criteriaBuilder.equal(root.get("salary").as(String.class),recruit.getSalary()));
                }
                if(!StringUtils.isBlank(recruit.getCondition())){
                    list.add(criteriaBuilder.equal(root.get("condition").as(String.class),recruit.getCondition()));
                }
                if(!StringUtils.isBlank(recruit.getEducation())){
                    list.add(criteriaBuilder.equal(root.get("education").as(String.class),recruit.getEducation()));
                }

                Predicate[] predicates = new Predicate[list.size()];
                predicates = list.toArray(predicates);

                return criteriaBuilder.and(predicates);
            }
        },pageable);

    }
}
