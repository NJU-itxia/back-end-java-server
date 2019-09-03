package com.itxia.backend.service;

import com.itxia.backend.controller.vo.WrapperResponse;
import com.itxia.backend.data.model.Location;
import com.itxia.backend.data.model.Order;
import com.itxia.backend.data.model.OrderQuery;
import com.itxia.backend.data.repo.ItxiaMemberRepository;
import com.itxia.backend.data.repo.OrderQueryRepository;
import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.HashMap;
import java.util.stream.Stream;

/**
 * @author Yzh
 */
@Service
@Transactional(rollbackFor = {Throwable.class})
public class AdminOrderService {

    private final OrderQueryRepository orderQueryRepository;

    private final ItxiaMemberRepository itxiaMemberRepository;

    private final Logger logger = LoggerFactory.getLogger(AdminService.class);

    @Autowired
    public AdminOrderService(OrderQueryRepository orderQueryRepository, ItxiaMemberRepository itxiaMemberRepository) {
        this.orderQueryRepository = orderQueryRepository;
        this.itxiaMemberRepository = itxiaMemberRepository;
    }

    public WrapperResponse searchOrder(String location, String state, String search, Integer pageNum, Integer pageSize,
                                       String handler) {
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
        if (status == Order.Status.ACCEPTED) {
            return searchOrderWithHandlerFirst(locationEnum, status, search, pageNum, pageSize, handler);
        } else {
            return searchOrderNormal(locationEnum, status, search, pageNum, pageSize);
        }
    }

    public WrapperResponse searchOrderWithHandlerFirst(Location location, Order.Status status, String search, Integer pageNum,
                                                       Integer pageSize, String handler) {
        final String searchString = search == null ? "%%" : "%" + search.toUpperCase() + "%";
        Specification<OrderQuery> specification = getSpecification(location, status, searchString);
        var result = orderQueryRepository.findAll(specification);
        result.sort((o1, o2) -> {
            if (o1.isHandler(handler) && !o2.isHandler(handler)) {
                return 1;
            } else if (o2.isHandler(handler) && !o1.isHandler(handler)) {
                return -1;
            } else {
                return o2.getTime().compareTo(o1.getTime());
            }
        });
        int size = result.size();
        int toIndex = ((pageNum + 1) * pageSize);
        if (toIndex > size) {
            toIndex = size;
        }
        result = result.subList(pageNum * pageSize, toIndex);
        Page<OrderQuery> page = new PageImpl<>(result, PageRequest.of(pageNum, pageSize), size);
        return WrapperResponse.wrap(page);
    }

    /**
     * 按照校区，维修单状态，搜索的字符串，页码和页数来查询维修单
     *
     * @param location 校区
     * @param status   维修单状态
     * @param search   查询字符串
     * @param pageNum  页码
     * @param pageSize 页的大小
     * @return 查询结果
     */
    public WrapperResponse searchOrderNormal(Location location, Order.Status status, String search, Integer pageNum, Integer pageSize) {
        final String searchString = search == null ? "%%" : "%" + search.toUpperCase() + "%";
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.Direction.DESC, "dateTime");
        Specification<OrderQuery> specification = getSpecification(location, status, searchString);
        var result = orderQueryRepository.findAll(specification, pageable);
        return WrapperResponse.wrap(result);
    }

    /**
     * 查询维修单的数量
     *
     * @param location 校区
     * @param search   搜索的字符串
     * @return 查询结果
     */
    public WrapperResponse getSearchNumber(String location, String search) {
        Location locationEnum = Location.fromValue(location);
        if (locationEnum == Location.UNDEFINED) {
            logger.info("校区错误: " + location);
            return WrapperResponse.wrapFail();
        }
        if ("null".equals(search)) {
            search = null;
        }
        final String searchString = search == null ? "%%" : "%" + search.toUpperCase() + "%";
        Order.Status[] statusList = new Order.Status[]{Order.Status.CREATED, Order.Status.ACCEPTED, Order.Status.FINISHED};
        HashMap<String, Long> result = new HashMap<>(3);
        for (Order.Status status : statusList) {
            Specification<OrderQuery> specification = getSpecification(locationEnum, status, searchString);
            result.put(status.getDescription(), orderQueryRepository.count(specification));
        }
        return WrapperResponse.wrap(result);
    }

    private Specification<OrderQuery> getSpecification(Location location, Order.Status status, String searchString) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Path<String> locationPath = root.get("location");
            Path<String> statusPath = root.get("status");
            Predicate locationPredict = criteriaBuilder.equal(locationPath, location.getValue());
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

            if (location == Location.ALL) {
                return criteriaBuilder.and(statusPredict, searchPredict);
            } else {
                return criteriaBuilder.and(locationPredict, statusPredict, searchPredict);
            }
        };
    }
}
