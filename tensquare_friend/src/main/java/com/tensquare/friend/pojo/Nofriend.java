package com.tensquare.friend.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @program: tensquare_parent
 * @description:
 * @author: 好人
 * @date: 2019-12-28 12:13
 **/
@Entity
@Table(name = "tb_nofriend")
@IdClass(Nofriend.class)
public class Nofriend implements Serializable {
    @Id
    private String userid;
    @Id
    private String friendid;


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getFriendid() {
        return friendid;
    }

    public void setFriendid(String friendid) {
        this.friendid = friendid;
    }
}
