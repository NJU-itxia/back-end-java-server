package com.itxia.backend.service;

import com.itxia.backend.controller.vo.AppointmentParam;
import com.itxia.backend.controller.vo.WrapperResponse;
import com.itxia.backend.data.repo.OrderRepository;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

/**
 * 用户相关的服务
 */
@Service
@Transactional
public class CustomerService {

    private final OrderRepository orderRepository;

    @Autowired
    public CustomerService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * 用户新建一个预约
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
        return WrapperResponse.wrapFail();
    }

    /**
     * 用户取消预约
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
        return WrapperResponse.wrapFail();
    }

    /**
     * 获取用户的未完成的预约单
     * <p>
     * 任务：
     * 1. 查询用户未完成的预约单，若无，返回失败
     * 2. 返回预约单
     *
     * @param customerId 用户的id
     * @return 成功时，使用WrapperResponse.wrap封装获取到的预约单， 失败时，返回wrapFail
     */
    public WrapperResponse getCurrentAppointment(String customerId) {
        return WrapperResponse.wrapFail();
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
        return WrapperResponse.wrapFail();
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
        if (customerId == null) {
            return WrapperResponse.wrapFail();
        }
        var allAppointments = orderRepository.findAll();
        var result = allAppointments.stream()
                .filter(a -> customerId.equals(a.getCustomer()))
                .collect(Collectors.toList());
        return WrapperResponse.wrap(result);
    }
}
