package com.itxia.backend.service;

import com.itxia.backend.controller.vo.AppointmentParam;
import com.itxia.backend.controller.vo.WrapperResponse;
import com.itxia.backend.data.model.Order;
import com.itxia.backend.data.model.Reply;
import com.itxia.backend.data.repo.OrderRepository;
import com.itxia.backend.data.repo.ReplyRepository;
import lombok.var;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.stream.Collectors;

/**
 * 用户相关的服务
 */
@Service
@Transactional
public class CustomerService {

    private final OrderRepository orderRepository;

    private final ReplyRepository replyRepository;

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    public CustomerService(OrderRepository orderRepository, ReplyRepository replyRepository) {
        this.orderRepository = orderRepository;
        this.replyRepository = replyRepository;
    }

    /**
     * 用户新建一个预约
     * 还没有添加传文件的选项
     * 添加、修改预约时应该更新最后修改时间
     * <p>
     * 任务：
     * 1. 如果没有任何联系方式，返回错误
     * 2. 如果正常，使用orderRepository将数据存储至数据库
     *
     * @param customerId       用户id，可能是手机号或者微信号
     * @param appointmentParam 用户新建的预约信息
     * @return 返回成功或者失败，通过wrapFail或wrapSuccess返回
     */
    public WrapperResponse makeAppointment(String customerId, AppointmentParam appointmentParam) {
        if (StringUtils.isEmpty(customerId)) {
            logger.info("用户id为空");
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
        orderRepository.save(order);
        return WrapperResponse.wrapSuccess();
    }

    /**
     * 用户取消预约
     * 如果已经接单，联系接单的人取消？
     * 讨论一下已经接单是不是可以取消
     * <p>
     * 任务：
     * 1. 将预约从数据库取出，若无此订单，返回失败
     * 2. 检查预约是否为此用户的预约，若不是，返回失败
     * 3. 检查预约状态是否为 新创建 若不是，返回失败
     * 4. 将预约状态设置为 已取消 并使用orderRepository存储至数据库
     *
     * @param customerId    用户的id， 应该与预约中的用户电话一直
     * @param appointmentId 待删除的预约单的id
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
        if (!StringUtils.equals(order.getCustomer(), customerId)) {
            logger.info("不是此用户的预约单");
            return WrapperResponse.wrapFail();
        }
        if (order.getStatus() != Order.Status.CREATED) {
            logger.info("不是新创建的订单，不可取消");
            return WrapperResponse.wrapFail();
        }
        order.setStatus(Order.Status.CANCELED.getIndex());
        orderRepository.save(order);
        logger.info("订单取消成功");
        return WrapperResponse.wrapSuccess();
    }

    /**
     * 获取用户的未完成的预约单
     * 未完成的状态有 新创建 和 已接单
     * 上述两个状态可以修改
     * <p>
     * 任务：
     * 1. 查询用户未完成的预约单，若无，返回失败
     * 2. 返回预约单
     *
     * @param customerId 用户的id
     * @return 成功时，使用WrapperResponse.wrap封装获取到的预约单， 失败时，返回wrapFail
     */
    public WrapperResponse getCurrentAppointment(String customerId) {
        if(StringUtils.isEmpty(customerId)) {
            logger.info("用户id不能为空");
            return WrapperResponse.wrapFail();
        }
        var orders = orderRepository.findByUserId(customerId);
        orders = orders.stream().filter(o -> o.getStatus() == Order.Status.CREATED || o.getStatus() == Order.Status.ACCEPTED).collect(Collectors.toList());
        return WrapperResponse.wrap(orders);
    }

    /**
     * 用户修改未完成的订单
     * <p>
     * 任务：
     * 1. 取出用户未完成的订单，若无，返回失败
     * 2. 根据传入的参数，修改订单值
     * 3. 将修改后的订单存储
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
     * 获取用户的所有预约单，按时间离当前最近优先排序
     * 若没有，返回成功，数据为空列表
     * <p>
     * 任务：
     * 1. 查询用户的预约单
     * 2. 对预约单排序
     * 3. 返回
     *
     * @param customerId 用户id
     * @return 返回查询结果
     */
    public WrapperResponse getAppointments(String customerId) {
        System.out.println(customerId);
        if (customerId == null) {
            logger.info("用户不能为空");
            return WrapperResponse.wrapFail();
        }
        var allAppointments = orderRepository.findAll();
        var result = allAppointments.stream()
                .filter(a -> customerId.equals(a.getPhone()))
                .collect(Collectors.toList());
        return WrapperResponse.wrap(result);
    }

    /**
     * 用户对某个预约单的评论
     * 任务：
     * 1. 检查参数是否为空
     * 2. 取出对应的预约单
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
}
