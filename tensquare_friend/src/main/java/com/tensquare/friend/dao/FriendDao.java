package com.tensquare.friend.dao;

import com.tensquare.friend.pojo.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @program: tensquare_parent
 * @description:
 * @author: 好人
 * @date: 2019-12-28 12:52
 **/
public interface FriendDao extends JpaRepository<Friend,String> {

    public Friend findByUseridAndFriendid(String userId,String friendId);

    @Modifying
    @Query(value = "UPDATE tb_friend SET islike = ? WHERE userid = ? AND friendid = ?",nativeQuery = true)
    public void updateIslike(String islike,String userId,String friendId);

    @Modifying
    @Query(value = "DELETE FROM tb_friend WHERE userid = ? AND friendid = ?",nativeQuery = true)
    void deleteFriend(String userId, String friendId);
}
