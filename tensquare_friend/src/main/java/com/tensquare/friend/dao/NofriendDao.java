package com.tensquare.friend.dao;

import com.tensquare.friend.pojo.Friend;
import com.tensquare.friend.pojo.Nofriend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @program: tensquare_parent
 * @description:
 * @author: 好人
 * @date: 2019-12-28 12:52
 **/
public interface NofriendDao extends JpaRepository<Nofriend,String> {

    public Nofriend findByUseridAndFriendid(String userId, String friendId);

}
