package com.itxia.backend.service;

import com.itxia.backend.controller.vo.AppointmentParam;
import com.itxia.backend.controller.vo.WrapperResponse;
import com.itxia.backend.data.model.Order;
import com.itxia.backend.data.model.OrderQuery;
import com.itxia.backend.data.model.Reply;
import com.itxia.backend.data.repo.OrderQueryRepository;
import com.itxia.backend.data.repo.OrderRepository;
import com.itxia.backend.data.repo.ReplyRepository;
import lombok.var;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.stream.Collectors;

/**
 * @author Yzh
 * 用户相关的服务
 */
@Service
@Transactional(rollbackFor = Throwable.class)
public class CustomerService {

    private final OrderRepository orderRepository;

    private final OrderQueryRepository orderQueryRepository;

    private final ReplyRepository replyRepository;

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    public CustomerService(OrderRepository orderRepository, ReplyRepository replyRepository, OrderQueryRepository orderQueryRepository) {
        this.orderRepository = orderRepository;
        this.replyRepository = replyRepository;
        this.orderQueryRepository = orderQueryRepository;
    }

    /**
     * 用户预约一个维修单
     * 还没有添加传文件的选项
     * 添加、修改预约时应该更新最后修改时间(修改: 可能需要记录创建时间)
     * <p>
     * 任务：
     * 1. 如果没有任何联系方式，返回错误
     * 2. 如果正常，使用orderRepository将数据存储至数据库
     * 3. 返回OrderId
     *
     * @param customerId       用户id，可能是手机号或者微信号
     * @param appointmentParam 用户新建的维修单信息
     * @return 返回成功或者失败，通过wrapFail或wrapSuccess返回
     */
    public WrapperResponse makeAppointment(String customerId, AppointmentParam appointmentParam) {
        if (StringUtils.isEmpty(customerId)) {
            logger.info("用户id为空");
            return WrapperResponse.wrapFail();
        }
        boolean unfinished = orderRepository.findByUserId(customerId).stream().anyMatch(o -> o.getStatus().isUnfinished());
        if (unfinished) {
            logger.info("用户存在未完成的维修单");
            return WrapperResponse.wrapFail();
        }
        Order order = Order.builder()
                .phone(customerId)
                .customer(appointmentParam.getName())
                .email(appointmentParam.getEmail())
                .locationRawValue(appointmentParam.getCampus())
                .deviceModel(appointmentParam.getDeviceVersion())
                .osVersion(appointmentParam.getSystemVersion())
                .problemDescription(appointmentParam.getDescription())
                .lastEditTime(new Timestamp(System.currentTimeMillis()))
                .build();
        order.setStatus(Order.Status.CREATED.getIndex());
        order = orderRepository.save(order);
        return WrapperResponse.wrap(order.getId());
    }

    /**
     * 用户取消预约
     * 如果已经接单，联系接单的人取消？
     * 讨论一下已经接单是不是可以取消
     * 可以取消
     * <p>
     * 任务：
     * 1. 将维修单从数据库取出，若无此维修单，返回失败
     * 2. 检查维修单是否由此用户预约，若不是，返回失败
     * 3. 检查维修单状态是否为 未完成 若不是，返回失败
     * 4. 将维修单状态设置为 已取消 并使用orderRepository存储至数据库
     *
     * @param customerId    用户的id， 应该与预约中的用户电话一直
     * @param appointmentId 待删除的维修单的id
     * @return 返回操作结果
     */
    public WrapperResponse deleteAppointment(String customerId, int appointmentId) {
        if (StringUtils.isEmpty(customerId)) {
            logger.info("用户id为空");
            return WrapperResponse.wrapFail();
        }
        var order = orderRepository.findById(appointmentId).orElse(null);
        if (order == null) {
            logger.info("没有此预约单");
            return WrapperResponse.wrapFail();
        }
        if (!StringUtils.equals(order.getPhone(), customerId)) {
            logger.info("不是此用户的预约单");
            return WrapperResponse.wrapFail();
        }
        if (!order.getStatus().isUnfinished()) {
            logger.info("不是未完成的维修单，不可取消");
            return WrapperResponse.wrapFail();
        }
        order.setStatus(Order.Status.CANCELED.getIndex());
        orderRepository.save(order);
        logger.info("订单取消成功");
        return WrapperResponse.wrapSuccess();
    }

    /**
     * 用户取消当前预约的维修单
     * 未完成的维修单则可以取消
     * 如果出现了异常，比如不止一个维修单是未完成，则全部取消
     * <p>
     * 任务：
     * 1. 检查用户ID是否正常，不正常返回失败
     * 2. 检查是否有未完成的维修单，若否返回失败
     * 3. 取出未完成的维修单，状态置为已取消
     * 4. 将维修单状态保存
     *
     * @param customerId 用户的id
     * @return 返回取消结果，成功true，失败false
     */
    public WrapperResponse deleteCurrentAppointment(String customerId) {
        if (StringUtils.isEmpty(customerId)) {
            logger.info("用户id不能为空");
            return WrapperResponse.wrapFail();
        }
        var orders = orderRepository.findByUserId(customerId);
        orders = orders.stream().filter(o -> o.getStatus().isUnfinished()).collect(Collectors.toList());
        if (orders.size() == 0) {
            logger.info("用户没有未完成的维修单");
            return WrapperResponse.wrapFail();
        } else {
            orders.forEach(o -> {
                o.setStatus(Order.Status.CANCELED.getIndex());
                orderRepository.save(o);
            });
        }
        logger.info("取消成功");
        return WrapperResponse.wrap(orders);
    }

    /**
     * 获取用户的未完成的维修单
     * 应该改成获取唯一一个而不是多个（未完成）
     * 未完成的状态有 新创建 和 已接单
     * 上述两个状态可以修改
     * <p>
     * 任务：
     * 1. 查询用户未完成的维修单，若无，返回失败
     * 2. 返回预约单
     *
     * @param customerId 用户的id
     * @return 成功时，使用WrapperResponse.wrap封装获取到的预约单， 失败时，返回wrapFail
     */
    public WrapperResponse getCurrentAppointment(String customerId) {
        if (StringUtils.isEmpty(customerId)) {
            logger.info("用户id不能为空");
            return WrapperResponse.wrapFail();
        }
        var orders = orderRepository.findByUserId(customerId);
        orders = orders.stream().filter(o -> o.getStatus() == Order.Status.CREATED || o.getStatus() == Order.Status.ACCEPTED).collect(Collectors.toList());
        return WrapperResponse.wrap(orders);
    }

    /**
     * 用户修改未完成的维修单
     * <p>
     * 任务：
     * 1. 取出用户未完成的维修单，若无，返回失败
     * 2. 根据传入的参数，修改订单值
     * 3. 将修改后的维修单存储
     *
     * @param customerId       用户id
     * @param appointmentParam 订单参数
     * @return 返回成功或失败
     */
    public WrapperResponse modifyAppointment(String customerId, AppointmentParam appointmentParam) {
        if (StringUtils.isEmpty(customerId) || appointmentParam == null) {
            logger.info("参数不能为空");
            return WrapperResponse.wrapFail();
        }
        var order = orderRepository.findByUserId(customerId).stream()
                .filter(o -> o.getStatus() == Order.Status.CREATED || o.getStatus() == Order.Status.ACCEPTED)
                .findFirst()
                .orElse(null);
        if (order == null) {
            logger.info("没有未完成的预约单");
            return WrapperResponse.wrapFail();
        }
        order.setLocation(appointmentParam.getCampus());
        order.setDeviceModel(appointmentParam.getDeviceVersion());
        order.setOsVersion(appointmentParam.getSystemVersion());
        order.setLastEditTime(new Timestamp(System.currentTimeMillis()));
        order.setProblemDescription(appointmentParam.getDescription());
        logger.info("修改完成");
        return WrapperResponse.wrapSuccess();
    }

    /**
     * 获取用户的所有维修单，按时间离当前最近优先排序
     * 若没有，返回成功，数据为空列表
     * 考虑一下分页的方案
     * <p>
     * 任务：
     * （考虑交给JPA）
     * 1. 查询用户的维修单
     * 2. 对维修单排序
     * 3. 返回
     *
     * @param customerId 用户id
     * @return 返回查询结果
     */
    public WrapperResponse getAppointments(String customerId, int pageNum, int pageSize) {
        if (customerId == null) {
            logger.info("用户不能为空");
            return WrapperResponse.wrapFail();
        }
        OrderQuery example = OrderQuery.builder()
                .phone(customerId).build();
        var allAppointments = orderQueryRepository.findAll(
                Example.of(example),
                PageRequest.of(pageNum, pageSize, Sort.Direction.DESC, "dateTime")
        );
        return WrapperResponse.wrap(allAppointments);
    }

    /**
     * 用户对某个维修单的评论
     * 任务：
     * 1. 检查参数是否为空
     * 2. 取出对应的维修单
     * 3. 如果没有，返回失败
     * 4. 如果有，添加一个回复
     *
     * @param customerId    用户的id
     * @param appointmentId 预约单的id
     * @param content       回复内容
     * @return 返回操作结果
     */
    public WrapperResponse commentOnAppointment(String customerId, int appointmentId, String content) {
        if (StringUtils.isEmpty(customerId) || StringUtils.isEmpty(content)) {
            logger.info("参数不能为空");
            return WrapperResponse.wrapFail();
        }
        var order = orderRepository.findById(appointmentId).orElse(null);
        if (order == null) {
            logger.info("没有此订单");
            return WrapperResponse.wrapFail();
        }
        if (!StringUtils.equals(customerId, order.getPhone())) {
            logger.info("不是此用户的预约单");
            return WrapperResponse.wrapFail();
        }
        Reply reply = Reply.builder()
                .itxiaReply(false)
                .replyTime(new Timestamp(System.currentTimeMillis()))
                .content(content)
                .orderId(appointmentId)
                .build();
        replyRepository.save(reply);
        logger.info("新的用户评论已添加");
        return WrapperResponse.wrapSuccess();
    }

    public WrapperResponse getAppointmentNum(String customerId) {
        if (customerId == null) {
            logger.info("用户不能为空");
            return WrapperResponse.wrapFail();
        }
        OrderQuery example = OrderQuery.builder()
                .phone(customerId).build();
        var allAppointments = orderQueryRepository.findAll(
                Example.of(example),
                Pageable.unpaged()
        );
        return WrapperResponse.wrap(allAppointments.getTotalElements());
    }
}
