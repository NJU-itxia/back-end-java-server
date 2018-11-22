package com.itxia.backend.data.repo;

import com.itxia.backend.data.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Yzh
 */
public interface OrderRepository extends JpaRepository<Order, Integer>, PagingAndSortingRepository<Order, Integer> {

    /**
     * 根据登录名查询
     *
     * @param userId 登录名
     * @return 返回查询结果
     */
    @Query("from Order o where o.phone = :userId")
    List<Order> findByUserId(@Param("userId") String userId);

    /**
     * 问题1： 这样查完之后修改会出问题，可能要改dao
     *
     * @param startTIme 开始时间
     * @param endTime   结束时间
     * @return 暂时不填
     */
    @Query("from Order o where o.lastEditTime between :startTime and :endTime")
    List<Order> findByLastEditTimeBetween(@Param("startTime") Timestamp startTIme, @Param("endTime") Timestamp endTime);

}
