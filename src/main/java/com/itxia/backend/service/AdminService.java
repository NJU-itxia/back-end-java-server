package com.itxia.backend.service;

import com.itxia.backend.controller.vo.WrapperResponse;
import com.itxia.backend.data.repo.ItxiaMemberRepository;
import com.itxia.backend.data.repo.OrderRepository;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 管理员账号的相关操作服务
 * 逻辑请在此处编写
 * 如果需要repository提供更多的条件查询功能，联系数据层的负责人
 */
@Service
@Transactional
public class AdminService {

    private final OrderRepository orderRepository;

    private final ItxiaMemberRepository itxiaMemberRepository;

    @Autowired
    public AdminService(OrderRepository orderRepository, ItxiaMemberRepository itxiaMemberRepository) {
        this.orderRepository = orderRepository;
        this.itxiaMemberRepository = itxiaMemberRepository;
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
     * <p>
     * 任务：
     * 1. 检查是否有重名用户，如果有，返回失败
     * 2. 用户名，密码不得为null或者""
     * 3. 添加成功则返回成功
     *
     * @param username 用户名
     * @param password 密码
     * @return 操作结果，使用WrapperResponse中的静态方法包装
     */
    public WrapperResponse createMember(String username, String password) {
        return WrapperResponse.wrapFail();
    }

    /**
     * 获取所有成员
     * <p>
     * 任务：
     * 1. 使用itxiaMemberRepository获取所有成员
     * 2. 使用WrapperResponse封装后返回
     * 3. 成员列表为空也返回正确，列表为空即可
     *
     * @return 操作结果
     */
    public WrapperResponse listAllMembers() {
        var result = itxiaMemberRepository.findAll();
        return WrapperResponse.wrap(result);
    }

    /**
     * 修改成员密码
     * <p>
     * 任务：
     * 1. 用户不存在或者两个参数为空时，返回失败
     * 2. 与原密码一致时，返回失败
     * 3. 修改密码后，将数据存储至数据库
     * 4. 返回修改成功
     *
     * @param memberId    成员ID
     * @param newPassword 新密码
     * @return 返回操作结果
     */
    public WrapperResponse modifyMemberPassword(String memberId, String newPassword) {
        return WrapperResponse.wrapFail();
    }
}
