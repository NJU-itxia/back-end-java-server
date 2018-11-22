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

    @Query("from Order o where o.phone = :userId")
    List<Order> findByUserId(@Param("userId") String userId);

    /**
     * 问题1： 这样查完之后修改会出问题，可能要改dao
     *
     * @return 暂时不填
     */
    @Query("from Order o where o.lastEditTime between :startTime and :endTime")
    List<Order> findByLastEditTimeBetween(@Param("startTime") Timestamp startTIme, @Param("endTime") Timestamp endTime);

}
