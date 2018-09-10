package com.itxia.backend.service;

import com.itxia.backend.controller.vo.WrapperResponse;
import com.itxia.backend.data.repo.OrderRepository;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 管理员账号的相关操作服务
 */
@Service
@Transactional
public class AdminService {

    private final OrderRepository orderRepository;

    @Autowired
    public AdminService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * 获取所有的预约单
     *
     * @return 返回封装后的预约单
     */
    public WrapperResponse listAllOrder() {
        var result = orderRepository.findAll();
        return WrapperResponse.wrap(result);
    }

    /**
     * 创建IT侠用户
     * 任务：
     * 暂无
     *
     * @param username 用户名
     * @param password 密码
     * @return 操作结果
     */
    public WrapperResponse createMember(String username, String password) {
        return WrapperResponse.wrapFail();
    }

    /**
     * 获取所有成员
     * <p>
     * 任务：
     * 暂无
     *
     * @return 操作结果
     */
    public WrapperResponse listAllMembers() {
        return WrapperResponse.wrapFail();
    }

    /**
     * 修改成员密码
     * <p>
     * 任务：
     * 暂无
     *
     * @param memberId    成员ID
     * @param newPassword 新密码
     * @return 返回操作结果
     */
    public WrapperResponse modifyMemberPassword(String memberId, String newPassword) {
        return WrapperResponse.wrapFail();
    }
}
