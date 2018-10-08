package com.itxia.backend.data.repo;

import com.itxia.backend.data.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("from Order o where o.phone = :userId")
    List<Order> findByUserId(@Param("userId") String userId);
}
