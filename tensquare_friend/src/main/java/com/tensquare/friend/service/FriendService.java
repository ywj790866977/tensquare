package com.tensquare.friend.service;

import com.tensquare.friend.dao.FriendDao;
import com.tensquare.friend.dao.NofriendDao;
import com.tensquare.friend.pojo.Friend;
import com.tensquare.friend.pojo.Nofriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: tensquare_parent
 * @description:
 * @author: 好人
 * @date: 2019-12-28 12:40
 **/
@Service
@Transactional
public class FriendService {

    @Autowired
    private FriendDao friendDao;

    @Autowired
    private NofriendDao nofriendDao;

    public int addFriend(String userId, String friendId) {
        //查询
        Friend friend = friendDao.findByUseridAndFriendid(userId, friendId);
        //判断
        if(friend != null){
            return 0;
        }
        //直接添加
//        friend.setUserid(userId);
        friend = new Friend();
        friend.setUserid(userId);
        friend.setFriendid(friendId);
        friend.setIslike("0");
        friendDao.save(friend);
        //判断双方关系
        if(friendDao.findByUseridAndFriendid(friendId, userId) != null){
            //修改双方关系为1
            friendDao.updateIslike("1",userId,friendId);
            friendDao.updateIslike("1",friendId,userId);
        }
        return 1;
    }

    public int addNofriend(String userId, String friendId) {
        Nofriend nofriend = nofriendDao.findByUseridAndFriendid(userId, friendId);
        if(nofriend !=null){
         return 0;
        }
        nofriend = new Nofriend();
        nofriend.setUserid(userId);
        nofriend.setFriendid(friendId);
        nofriendDao.save(nofriend);
        return 1;
    }

    public void deleteFriend(String userId, String friendId) {
        //删除userid到friendid的状态
        friendDao.deleteFriend(userId,friendId);
        //更新friendid到userid 的状态
        friendDao.updateIslike("0",friendId,userId);
        //添加非好友状态
        Nofriend nofriend = new Nofriend();
        nofriend.setUserid(userId);
        nofriend.setFriendid(friendId);
        nofriendDao.save(nofriend);
    }
}
