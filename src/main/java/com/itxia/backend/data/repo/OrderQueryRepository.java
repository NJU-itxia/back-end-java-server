package com.itxia.backend.data.repo;

import com.itxia.backend.data.model.OrderQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Yzh
 */
public interface OrderQueryRepository extends JpaRepository<OrderQuery, Integer>,
        PagingAndSortingRepository<OrderQuery, Integer>,
        JpaSpecificationExecutor<OrderQuery> {

}
