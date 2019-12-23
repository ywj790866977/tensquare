package com.tensquare.recruit.dao;

import com.tensquare.recruit.pojo.Recruit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @program: tensquare_parent
 * @description:
 * @author: 好人
 * @date: 2019-12-22 15:42
 **/
public interface RecruitDao extends JpaSpecificationExecutor<Recruit>, JpaRepository<Recruit,String> {
    /**
     * @Description: 根据状态查询前6条,并根据时间排序
     * @param: state
     * @Return: java.util.List<com.tensquare.recruit.pojo.Recruit>
     * @Date: 2019-12-22 16:58
     */
    List<Recruit> findTop6ByStateOrderByCreatetimeDesc(String state);

    /**
     * @Description: 查询非推荐的前6个
     * @param: state
     * @Return: java.util.List<com.tensquare.recruit.pojo.Recruit>
     * @Date: 2019-12-22 17:03
     */
    List<Recruit> findTop6ByStateNotOrderByCreatetimeDesc(String state);

}
