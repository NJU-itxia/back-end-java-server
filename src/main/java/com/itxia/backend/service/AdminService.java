package com.itxia.backend.service;

import com.itxia.backend.controller.vo.WrapperResponse;
import com.itxia.backend.data.model.ItxiaMember;
import com.itxia.backend.data.model.Location;
import com.itxia.backend.data.model.Order;
import com.itxia.backend.data.model.OrderQuery;
import com.itxia.backend.data.repo.ItxiaMemberRepository;
import com.itxia.backend.data.repo.OrderQueryRepository;
import com.itxia.backend.data.repo.OrderRepository;
import com.itxia.backend.util.PasswordUtil;
import lombok.var;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
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

    private final OrderQueryRepository orderQueryRepository;

    private final ItxiaMemberRepository itxiaMemberRepository;

    private final Logger logger = LoggerFactory.getLogger(AdminService.class);

    @Autowired
    public AdminService(OrderRepository orderRepository, ItxiaMemberRepository itxiaMemberRepository, OrderQueryRepository orderQueryRepository) {
        this.orderRepository = orderRepository;
        this.orderQueryRepository = orderQueryRepository;
        this.itxiaMemberRepository = itxiaMemberRepository;
    }

    /**
     * 获取所有的维修单
     *
     * @return 返回封装后的维修单
     */
    public WrapperResponse listAllOrder() {
        var result = orderRepository.findAll(Pageable.unpaged());
        return WrapperResponse.wrap(result);
    }

    /**
     * 创建IT侠用户
     * <p>
     * 任务：
     * 1. 检查是否有重名用户，如果有，返回失败
     * 2. 用户名不得为null或者""
     * 3. 密码应符合制定的规范
     * 4. 添加成功则返回成功
     *
     * @param username 用户名
     * @param password 密码
     * @return 操作结果，使用WrapperResponse中的静态方法包装
     */
    public WrapperResponse createMember(String username, String password, Location location, String name) {
        var testMember = itxiaMemberRepository.findOneByLoginName(username);
        if (StringUtils.isEmpty(name) || location == null) {
            logger.info("校区和登录名不能为空");
            return WrapperResponse.wrapFail();
        }
        if (testMember != null) {
            logger.info("创建的用户已存在");
            return WrapperResponse.wrapFail();
        }
        if (!PasswordUtil.isValidPassword(password)) {
            logger.info("密码不符合要求");
            return WrapperResponse.wrapFail();
        }
        ItxiaMember member = ItxiaMember.builder()
                .admin(false)
                .loginName(username)
                .name(name)
                .password(password)
                .email(null)
                .locationRawValue(location.getValue())
                .build();
        itxiaMemberRepository.save(member);
        logger.info("用户创建成功");
        return WrapperResponse.wrapSuccess();
    }

    /**
     * 获取所有成员
     * <p>
     * 任务：
     * 1. 使用itxiaMemberRepository获取所有成员
     * 2. 使用WrapperResponse封装后返回
     * 3. 成员列表为空也返回正确，列表为空即可
     * 4. 权限都没控制。。
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
        if (StringUtils.isEmpty(memberId) || StringUtils.isEmpty(newPassword)) {
            logger.info("参数为空: memberId: " + memberId + " newPassword: " + newPassword);
            return WrapperResponse.wrapFail();
        }
        if (!PasswordUtil.isValidPassword(newPassword)) {
            logger.info("密码不符合规格");
            return WrapperResponse.wrapFail();
        }
        var member = itxiaMemberRepository.findOneByLoginName(memberId);
        if (member == null) {
            logger.info("itxiaMember 不存在");
            return WrapperResponse.wrapFail();
        }
        if (newPassword.equals(member.getPassword())) {
            logger.info("修改密码时，新密码和原密码的相同");
            return WrapperResponse.wrapFail("不能修改为同样的密码");
        }
        member.setPassword(newPassword);
        itxiaMemberRepository.save(member);
        return WrapperResponse.wrapSuccess();
    }

    /**
     * 查询分页的维修单
     * 按照最后修改时间降序排列
     *
     * @param pageNum  页码
     * @param pageSize 页的大小
     * @return 查询结果
     */
    public WrapperResponse listOrderBy(Integer pageNum, Integer pageSize) {
        var result = orderRepository.findAll(
                PageRequest.of(
                        pageNum,
                        pageSize,
                        Sort.Direction.DESC,
                        "lastEditTime"));
        return WrapperResponse.wrap(result);
    }

    /**
     * 查询校区、订单状态、搜索字符串的分页的维修单
     *
     * @param location 校区
     * @param state    订单状态
     * @param search   搜索字符串
     * @param pageNum  页码
     * @param pageSize 页的大小
     * @return 查询结果
     */
    public WrapperResponse listOrderBy(String location, String state, String search, Integer pageNum, Integer pageSize) {
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
        System.out.println(search);
        OrderQuery example = OrderQuery.builder()
                .location(location)
                .status(status.getIndex())
                .problemDescription(search).build();
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("location", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("status", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("problemDescription", ExampleMatcher.GenericPropertyMatchers.contains());
        var result = orderQueryRepository.findAll(
                Example.of(example, exampleMatcher),
                PageRequest.of(pageNum, pageSize, Sort.Direction.DESC, "dateTime")
        );
        return WrapperResponse.wrap(result);
    }
}
