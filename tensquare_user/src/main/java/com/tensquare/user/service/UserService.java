package com.tensquare.user.service;


import com.tensquare.user.dao.UserDao;
import com.tensquare.user.pojo.User;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;
import util.JwtUtil;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Service
@Transactional
public class UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private HttpServletRequest request;

    public List<User> findAll(){
        return userDao.findAll();
    }

    public User findById(String id){
        return userDao.findById(id).get();
    }

    public void save(User user){
        user.setId(idWorker.nextId()+"");
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword())); //加密
        user.setFollowcount(0);
        user.setFanscount(0);
        user.setOnline(0L);
        user.setRegdate(new Date());
        user.setUpdatedate(new Date());
        user.setLastdate(new Date());
        userDao.save(user);
    }

    public void update(User user){
        userDao.save(user);
    }

    /**
     * @Description: deleteById
     * @param: id
     * @Return: void
     * @Date: 2019-12-26 22:58
     */
    public void deleteById(String id){
        //判断有无权限
        String token = (String) request.getAttribute("claims_admin");
        if(StringUtils.isEmpty(token)){
            throw new RuntimeException("权限不足!");
        }
        userDao.deleteById(id);
    }

    /**
     * @Description: 条件查询
     * @param: label
     * @Return: java.util.List<com.tensquare.base.pojo.Admin>
     * @Date: 2019-12-20 16:52
     */
    public List<User> findSearch(User user) {
        return userDao.findAll(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if(!StringUtils.isBlank(user.getNickname())){
                    list.add(criteriaBuilder.like(root.get("nickname").as(String.class),"%"+user.getNickname()+"%"));
                }
                if(!StringUtils.isBlank(user.getMobile())){
                    list.add(criteriaBuilder.equal(root.get("mobile").as(String.class),user.getMobile()));
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
     * @Return: org.springframework.data.domain.Page<com.tensquare.base.pojo.Admin>
     * @Date: 2019-12-20 17:12
     */
    public Page<User> pageQuery(User user   , int page, int size) {
        //添加分页
        Pageable pageable = PageRequest.of(page-1,size);
        //添加条件
        return userDao.findAll(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if(!StringUtils.isBlank(user.getNickname())){
                    list.add(criteriaBuilder.like(root.get("nickname").as(String.class),"%"+user.getNickname()+"%"));
                }
                if(!StringUtils.isBlank(user.getMobile())){
                    list.add(criteriaBuilder.equal(root.get("mobile").as(String.class),user.getMobile()));
                }

                Predicate[] predicates = new Predicate[list.size()];
                predicates = list.toArray(predicates);

                return criteriaBuilder.and(predicates);
            }
        },pageable);

    }

    /**
     * @Description: sendSms
     * @param: mobile
     * @Return: void
     * @Date: 2019-12-25 21:48
     */
    public void sendSms(String mobile) {
        //生成随机数字
        String checkCode = RandomStringUtils.randomNumeric(6);
        //放入缓存
        redisTemplate.opsForValue().set("checkCode_"+mobile,checkCode,6, TimeUnit.HOURS);
        //发送给用户
        Map<String,String> map  = new HashMap<>();
        map.put("mobile",mobile);
        map.put("checkCode",checkCode);
//        rabbitTemplate.convertAndSend("sms",map);
        //打印
        System.out.println("验证码: "+checkCode);
    }


    /**
     * @Description: login
     * @param: mobile
     * @param: password
     * @Return: com.tensquare.user.pojo.User
     * @Date: 2019-12-26 17:34
     */
    public User login(String mobile, String password) {
        User loginUser = userDao.findByMobile(mobile);
        if(loginUser != null && bCryptPasswordEncoder.matches(password,loginUser.getPassword())){
            return loginUser;
        }
        return null;
    }

    public void updateFanscountAndFollowcount(String userid, String friendid, int x) {
        userDao.updateFanscount(x,friendid);
        userDao.updateFollowcount(x,userid);
    }
}
