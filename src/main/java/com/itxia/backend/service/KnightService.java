package com.itxia.backend.service;

import com.itxia.backend.controller.vo.WrapperResponse;
import com.itxia.backend.data.repo.ItxiaMemberRepository;
import com.itxia.backend.data.repo.OrderRepository;
import lombok.var;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 非管理员it侠用户相关操作的服务
 */
@Service
@Transactional
public class KnightService {

    private final ItxiaMemberRepository itxiaMemberRepository;

    private final OrderRepository orderRepository;

    @Autowired
    public KnightService(ItxiaMemberRepository itxiaMemberRepository, OrderRepository orderRepository) {
        this.itxiaMemberRepository = itxiaMemberRepository;
        this.orderRepository = orderRepository;
    }

    /**
     * 修改自己的密码
     * <p>
     * 任务：
     * 1. 使用 itxiaMemberRepository 取出对应账号，若不存在，返回失败
     * 2. 两个密码为空时，返回失败
     * 3. 检查密码与oldPassword是否一致，若不一致，返回失败
     * 4. 修改密码并使用 itxiaMemberRepository 存储
     * 5. 返回成功
     *
     * @param knightId    IT侠账号
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 返回操作结果
     */
    public WrapperResponse modifyPassword(String knightId, String oldPassword, String newPassword) {
        if (StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(newPassword)) {
            return WrapperResponse.wrapFail();
        }
        var member = itxiaMemberRepository.findOneByLoginName(knightId);
        if (oldPassword.equals(member.getPassword())) {
            member.setPassword(newPassword);
            itxiaMemberRepository.save(member);
            return WrapperResponse.wrapSuccess();
        } else {
            return WrapperResponse.wrapFail();
        }
    }

    /**
     * 接单！
     * <p>
     * 任务：
     * 1. 取出it侠用户和订单，如果为空，返回失败
     * 2. 检查订单状态是否为 新创建 若否，返回失败
     * 3. 将it侠账号写入订单中，修改订单状态
     * 4. 存储订单
     *
     * @param knightId      IT侠账号
     * @param appointmentId 订单ID
     * @return 返回操作结果
     */
    public WrapperResponse acceptAppointment(String knightId, int appointmentId) {
        return WrapperResponse.wrapFail();
    }

    /**
     * 回复预约单
     * <p>
     * 任务：
     * 暂无，搁置
     *
     * @param knightId      IT侠账号
     * @param appointmentId 订单ID
     * @param content       回复内容
     * @return 返回操作结果
     */
    public WrapperResponse reply(String knightId, int appointmentId, String content) {
        return WrapperResponse.wrapFail();
    }
}
