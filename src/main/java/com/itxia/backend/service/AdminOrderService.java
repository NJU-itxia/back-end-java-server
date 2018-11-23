package com.itxia.backend.service;

import com.itxia.backend.controller.vo.WrapperResponse;
import com.itxia.backend.data.model.Location;
import com.itxia.backend.data.model.Order;
import com.itxia.backend.data.model.OrderQuery;
import com.itxia.backend.data.repo.OrderQueryRepository;
import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.stream.Stream;

/**
 * @author Yzh
 */
@Service
@Transactional(rollbackFor = {Throwable.class})
public class AdminOrderService {

    private final OrderQueryRepository orderQueryRepository;

    private final Logger logger = LoggerFactory.getLogger(AdminService.class);

    @Autowired
    public AdminOrderService(OrderQueryRepository orderQueryRepository) {
        this.orderQueryRepository = orderQueryRepository;
    }

    /**
     * @param location 校区
     * @param state    维修单状态
     * @param search   查询字符串
     * @param pageNum  页码
     * @param pageSize 页的大小
     * @return 查询结果
     */
    public WrapperResponse searchOrder(String location, String state, String search, Integer pageNum, Integer pageSize) {
        Location locationEnum = Location.fromValue(location);
        if (locationEnum == Location.UNDEFINED) {
            logger.info("校区错误: " + location);
            return WrapperResponse.wrapFail();
        }
        Order.Status status;
        try {
            status = Order.Status.valueOf(state);
        } catch (Exception e) {
            logger.info("错误的状态: " + state);
            return WrapperResponse.wrapFail();
        }
        if ("null".equals(search)) {
            search = null;
        }
        final String searchString = search == null ? "%%" : "%" + search.toUpperCase() + "%";
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.Direction.DESC, "dateTime");
        Specification<OrderQuery> specification = (root, criteriaQuery, criteriaBuilder) -> {
            Path<String> locationPath = root.get("location");
            Path<String> statusPath = root.get("status");
            Predicate locationPredict = criteriaBuilder.equal(locationPath, location);
            Predicate statusPredict = criteriaBuilder.equal(statusPath, status.getIndex());

            Predicate predicate = Stream.of("customer", "osVersion", "deviceModel", "email", "phone", "problemDescription")
                    .map(p -> {
                        Path<String> path = root.get(p);
                        return criteriaBuilder.like(criteriaBuilder.upper(path), searchString);
                    })
                    .reduce(criteriaBuilder::or).orElse(null);

            Path<String> handlerPath = root.join("itxia", JoinType.LEFT).get("name");
            Predicate handlerPredict = criteriaBuilder.like(criteriaBuilder.upper(handlerPath), searchString);
            Predicate searchPredict = criteriaBuilder.or(predicate, handlerPredict);

            return criteriaBuilder.and(locationPredict, statusPredict, searchPredict);
        };
        var result = orderQueryRepository.findAll(specification, pageable);
        return WrapperResponse.wrap(result);
    }
}
