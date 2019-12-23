package com.tensquare.recruit.dao;


import com.tensquare.recruit.pojo.Enterprise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @program: tensquare_parent
 * @description:
 * @author: 好人
 * @date: 2019-12-22 15:41
 **/
public interface EnterpriseDao extends JpaRepository<Enterprise,String>, JpaSpecificationExecutor<Enterprise> {
    public List<Enterprise> findByIshot(String ishot);
}
