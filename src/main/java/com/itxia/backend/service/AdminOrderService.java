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

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

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
            Path<String> namePath = root.get("customer");
            Path<String> osPath = root.get("osVersion");
            Path<String> devicePath = root.get("deviceModel");
            Path<String> emailPath = root.get("email");
            Path<String> phonePath = root.get("phone");
            Path<String> handlerPath = root.get("itxia").get("name");
            Path<String> problemDescription = root.get("problemDescription");
            Predicate locationPredict = criteriaBuilder.equal(locationPath, location);
            Predicate statusPredict = criteriaBuilder.equal(statusPath, status.getIndex());
            Predicate osPredict = criteriaBuilder.like(criteriaBuilder.upper(osPath), searchString);
            Predicate devicePredict = criteriaBuilder.like(criteriaBuilder.upper(devicePath), searchString);
            Predicate emailPredict = criteriaBuilder.like(criteriaBuilder.upper(emailPath), searchString);
            Predicate phonePredict = criteriaBuilder.like(criteriaBuilder.upper(phonePath), searchString);
            Predicate namePredict = criteriaBuilder.like(criteriaBuilder.upper(namePath), searchString);
            Predicate handlerPredict = criteriaBuilder.like(criteriaBuilder.upper(handlerPath), searchString);
            Predicate problemPredict = criteriaBuilder.like(criteriaBuilder.upper(problemDescription), searchString);
            Predicate searchPredict = criteriaBuilder.or(osPredict, devicePredict, emailPredict, phonePredict,
                    handlerPredict, problemPredict, namePredict);
            return criteriaBuilder.and(locationPredict, statusPredict, searchPredict);
        };
        var result = orderQueryRepository.findAll(specification, pageable);
        return WrapperResponse.wrap(result);
    }
}
