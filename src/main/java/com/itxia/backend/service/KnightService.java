package com.itxia.backend.service;

import com.itxia.backend.controller.vo.SelfInfoParam;
import com.itxia.backend.controller.vo.WrapperResponse;
import com.itxia.backend.data.model.Location;
import com.itxia.backend.data.model.Order;
import com.itxia.backend.data.model.Reply;
import com.itxia.backend.data.repo.ItxiaMemberRepository;
import com.itxia.backend.data.repo.OrderRepository;
import com.itxia.backend.data.repo.ReplyRepository;
import com.itxia.backend.util.PasswordUtil;
import lombok.var;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

/**
 * @author Yzh
 * 非管理员it侠用户相关操作的服务
 */
@Service
@Transactional(rollbackFor = Throwable.class)
public class KnightService {

    private final ItxiaMemberRepository itxiaMemberRepository;
    private final OrderRepository orderRepository;
    private final ReplyRepository replyRepository;

    private static final Logger logger = LoggerFactory.getLogger(KnightService.class);

    @Autowired
    public KnightService(ItxiaMemberRepository itxiaMemberRepository, OrderRepository orderRepository, ReplyRepository replyRepository) {
        this.itxiaMemberRepository = itxiaMemberRepository;
        this.orderRepository = orderRepository;
        this.replyRepository = replyRepository;
    }

    /**
     * 获取个人信息
     *
     * @param id IT侠账号ID
     * @return 查询结果
     */
    public WrapperResponse getSelfInfo(String id) {
        return WrapperResponse.wrap(itxiaMemberRepository.findOneByLoginName(id));
    }

    /**
     * 更新个人信息
     *
     * @param id IT侠账号ID
     * @param location 校区
     * @param acceptEmail 是否接受邮件
     * @param email 邮箱地址
     * @return 更新结果
     */
    public WrapperResponse updateSelfInfo(SelfInfoParam selfInfoParam) {
        var info = itxiaMemberRepository.findOneByLoginName(id);
        if(info==null){
            return WrapperResponse.wrapFail("该用户不存在");
        }
        var location = selfInfoParam.getLocation();
        if(location!=null && ! location.isEmpty()){
            //这波操作没有酒
            switch (location){
                case "鼓楼":
                case "GULOU":
                case "GU_LOU":
                    location = Location.GU_LOU.getValue();
                    break;
                case "仙林":
                case "XIANLIN":
                case "XIAN_LIN":
                    location = Location.XIAN_LIN.getValue();
                    break;
                case "ALL":
                case "全校":
                    location = Location.ALL.getValue();
                    break;
                default:
                    return WrapperResponse.wrapFail("校区不存在(开新校区了嘛)");
            }
            info.setLocationRawValue(location);
        }
        info.setAcceptEmail(selfInfoParam.getAcceptEmail());
        var email = selfInfoParam.getEmail();
        if(email!=null){
            //可为 ""
            info.setEmail(email);
        }
        itxiaMemberRepository.save(info);
        return WrapperResponse.wrapSuccess();
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
        if (oldPassword.equals(member.getPassword()) && PasswordUtil.isValidPassword(newPassword)) {
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
     * 1. 取出it侠用户和维修单，如果为空，返回失败
     * 2. 检查维修单状态是否为 新创建 若否，返回失败
     * 3. 将it侠账号写入维修单中，修改维修单状态
     * 4. 存储维修单
     *
     * @param knightId      IT侠账号
     * @param appointmentId 订单ID
     * @return 返回操作结果
     */
    public WrapperResponse acceptAppointment(String knightId, int appointmentId) {
        if (StringUtils.isEmpty(knightId)) {
            logger.info("IT侠成员id为空");
            return WrapperResponse.wrapFail();
        }
        var order = orderRepository.findById(appointmentId).orElse(null);
        if (order == null) {
            logger.info("预约单为空");
            return WrapperResponse.wrapFail();
        }
        var member = itxiaMemberRepository.findOneByLoginName(knightId);
        if (member == null) {
            logger.info("IT侠成员不存在");
            return WrapperResponse.wrapFail();
        }
        if (order.getStatus() != Order.Status.CREATED) {
            logger.info("订单状态不为新建状态");
            return WrapperResponse.wrapFail();
        }
        order.setStatus(Order.Status.ACCEPTED.getIndex());
        order.setHandler(member.getId());
        orderRepository.save(order);
        return WrapperResponse.wrapSuccess();
    }

    /**
     * 回复维修单
     * <p>
     * 任务：
     * 1. 如果参数为空，返回失败
     * 2. 根据id，取出维修单和it侠用户
     * 3. 如果为空，返回失败
     * 4. 添加一条回复
     * 5. 返回成功
     * <p>
     * 注意：
     * 1. 不一定要接了这个单的IT侠成员才可以恢复这个维修单
     * 2. 回复查看时将按照时间顺序查看
     *
     * @param knightId      IT侠账号
     * @param appointmentId 订单ID
     * @param content       回复内容
     * @return 返回操作结果
     */
    public WrapperResponse reply(String knightId, int appointmentId, String content) {
        logger.info("reply called with params: " + knightId + "," + appointmentId + "," + content);
        if (StringUtils.isEmpty(knightId) || StringUtils.isEmpty(content)) {
            logger.info("空的参数");
            return WrapperResponse.wrapFail();
        }
        var order = orderRepository.findById(appointmentId).orElse(null);
        if (order == null) {
            logger.info("没有这个维修单: " + appointmentId);
            return WrapperResponse.wrapFail();
        }
        var member = itxiaMemberRepository.findOneByLoginName(knightId);
        if (member == null) {
            logger.info("没有这个IT侠账号: " + knightId);
            return WrapperResponse.wrapFail();
        }
        Reply reply = Reply.builder()
                .content(content)
                .itxia(member)
                .itxiaReply(true)
                .orderId(appointmentId)
                .replyTime(new Timestamp(System.currentTimeMillis()))
                .build();
        replyRepository.save(reply);
        logger.info("新的回复已保存");
        return WrapperResponse.wrapSuccess();
    }

    public WrapperResponse finishAppointment(String knightId, Integer appointmentId) {
        if (StringUtils.isEmpty(knightId)) {
            logger.info("IT侠成员id为空");
            return WrapperResponse.wrapFail();
        }
        var order = orderRepository.findById(appointmentId).orElse(null);
        if (order == null) {
            logger.info("预约单为空");
            return WrapperResponse.wrapFail();
        }
        var member = itxiaMemberRepository.findOneByLoginName(knightId);
        if (member == null) {
            logger.info("IT侠成员不存在");
            return WrapperResponse.wrapFail();
        }
        if (order.getStatus() != Order.Status.ACCEPTED) {
            logger.info("订单状态不为已接受状态");
            return WrapperResponse.wrapFail();
        }
        order.setStatus(Order.Status.FINISHED.getIndex());
        order.setHandler(member.getId());
        orderRepository.save(order);
        return WrapperResponse.wrapSuccess();
    }

    public WrapperResponse putBackAppointment(String knightId, Integer appointmentId) {
        if (StringUtils.isEmpty(knightId)) {
            logger.info("IT侠成员id为空");
            return WrapperResponse.wrapFail();
        }
        var order = orderRepository.findById(appointmentId).orElse(null);
        if (order == null) {
            logger.info("预约单为空");
            return WrapperResponse.wrapFail();
        }
        var member = itxiaMemberRepository.findOneByLoginName(knightId);
        if (member == null) {
            logger.info("IT侠成员不存在");
            return WrapperResponse.wrapFail();
        }
        if (order.getStatus() != Order.Status.ACCEPTED) {
            logger.info("订单状态不为已接受状态");
            return WrapperResponse.wrapFail();
        }
        order.setStatus(Order.Status.CREATED.getIndex());
        order.setHandler(null);
        orderRepository.save(order);
        return WrapperResponse.wrapSuccess();
    }
}
