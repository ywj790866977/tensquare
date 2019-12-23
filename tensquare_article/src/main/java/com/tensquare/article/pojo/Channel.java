package com.tensquare.article.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @program: tensquare_parent
 * @description:
 * @author: 好人
 * @date: 2019-12-23 10:52
 **/
@Entity
@Table(name = "tb_channel")
public class Channel {
    @Id
    private String id;
    private String name;
    private String state;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
