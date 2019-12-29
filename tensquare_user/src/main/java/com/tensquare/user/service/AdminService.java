package com.tensquare.user.service;

import com.tensquare.user.dao.AdminDao;
import com.tensquare.user.pojo.Admin;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
public class AdminService {
    @Autowired
    private AdminDao adminDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public List<Admin> findAll(){
        return adminDao.findAll();
    }

    public Admin findById(String id){
        return adminDao.findById(id).get();
    }

    public void save(Admin admin){
        admin.setId(idWorker.nextId()+"");
        //密码加密
        admin.setPassword(bCryptPasswordEncoder.encode(admin.getPassword()));
        adminDao.save(admin);
    }

    public void update(Admin admin){
        adminDao.save(admin);
    }

    public void deleteById(String id){
        adminDao.deleteById(id);
    }

    /**
     * @Description: 条件查询
     * @param: label
     * @Return: java.util.List<com.tensquare.base.pojo.Label>
     * @Date: 2019-12-20 16:52
     */
    public List<Admin> findSearch(Admin admin) {
        return adminDao.findAll(new Specification<Admin>() {
            @Override
            public Predicate toPredicate(Root<Admin> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if(!StringUtils.isBlank(admin.getLoginname())){
                    list.add(criteriaBuilder.like(root.get("loginname").as(String.class),"%"+admin.getLoginname()+"%"));
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
    public Page<Admin> pageQuery(Admin admin, int page, int size) {
        //添加分页
        Pageable pageable = PageRequest.of(page-1,size);
        //添加条件
        return adminDao.findAll(new Specification<Admin>() {
            @Override
            public Predicate toPredicate(Root<Admin> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if(!StringUtils.isBlank(admin.getLoginname())){
                    list.add(criteriaBuilder.like(root.get("loginname").as(String.class),"%"+admin.getLoginname()+"%"));
                }
                Predicate[] predicates = new Predicate[list.size()];
                predicates = list.toArray(predicates);

                return criteriaBuilder.and(predicates);
            }
        },pageable);

    }

    public Admin login(Admin admin) {
        //先查询用户
        Admin adminLogin =  adminDao.findByLoginname(admin.getLoginname());
        //查看密码是否匹配
        if(adminLogin != null && bCryptPasswordEncoder.matches(admin.getPassword(),adminLogin.getPassword())){
            return adminLogin;
        }
        return null;
    }
}
