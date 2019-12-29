package com.tensquare.user.dao;


import com.tensquare.user.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserDao extends JpaRepository<User,String>, JpaSpecificationExecutor<User>{
    public User findByMobile(String mobile);

    @Modifying
    @Query(value = "UPDATE tb_user SET fanscount = fanscount+? WHERE id = ?",nativeQuery = true)
    void updateFanscount(int x, String friendid);

    @Modifying
    @Query(value = "UPDATE tb_user SET followcount = followcount+? WHERE id = ?",nativeQuery = true)
    void updateFollowcount(int x, String userid);
}
