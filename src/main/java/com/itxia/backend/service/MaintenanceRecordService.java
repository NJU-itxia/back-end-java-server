package com.itxia.backend.service;

import com.aliyun.oss.OSSClient;
import com.itxia.backend.controller.vo.WrapperResponse;
import com.itxia.backend.data.model.ItxiaMember;
import com.itxia.backend.data.model.Order;
import com.itxia.backend.data.repo.ItxiaMemberRepository;
import com.itxia.backend.data.repo.OrderRepository;
import com.itxia.backend.util.TimeUtil;
import lombok.var;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Yzh
 * 每周的维修记录报表生成
 * <p>
 * 还没有测试
 */
@Service
@EnableScheduling
public class MaintenanceRecordService {

    /**
     * access key
     */
    @Value("${aliyun.key}")
    private String accessKey;

    /**
     * access key secret
     */
    @Value("${aliyun.secret}")
    private String accessKeySecret;

    @Value("${aliyun.oss.endpoint}")
    private String endPoint;

    @Value("${aliyun.oss.bucketname}")
    private String bucketName;

    private final OrderRepository orderRepository;

    private final ItxiaMemberRepository itxiaMemberRepository;

    private final MailService mailService;

    @Autowired
    public MaintenanceRecordService(OrderRepository orderRepository, ItxiaMemberRepository itxiaMemberRepository, MailService mailService) {
        this.orderRepository = orderRepository;
        this.itxiaMemberRepository = itxiaMemberRepository;
        this.mailService = mailService;
    }

    /**
     * 时间暂时定为每周一23点33分吧
     * <p>
     * 需要完成的任务：
     * <p>
     * 1. 查询是否已经生成过，否则直接生成
     * 2. 查询oss上是否确实有上一周的记录，如果没有，再生成一次
     * 3. 拿到上一周的所有预约单，按校区、时间排序
     * 4. 关键信息变*
     * 5. 生成word
     * 6. 该放util的放util,毕竟以后可能要直接显示
     */
    @Scheduled(cron = "0 33 23 ? * MON")
    private void generateMaintenanceRecord() {
        Timestamp startTime = TimeUtil.lastWeekStartTime();
        Timestamp endTime = TimeUtil.lastWeekEndTime();
        List<Order> orders = orderRepository.findByLastEditTimeBetween(startTime, endTime);
        String key = "maintenance_log/year" + TimeUtil.currentYearNum() + "_week" + TimeUtil.lastWeekNum() + ".txt";
        StringBuilder builder = new StringBuilder();
        orders.stream().map(this::formatOrder).forEach(builder::append);
        this.upload(key, builder.toString());
    }

    /**
     * 每分钟一次
     */
    @Scheduled(cron = "0 */1 * * * *")
    private void checkNewAppointment() {
        Timestamp startTime = new Timestamp(new DateTime().minusMinutes(1).getMillis());
        Timestamp endTime = new Timestamp(System.currentTimeMillis());
        var orders = orderRepository.findByLastEditTimeBetween(startTime, endTime);
        if(orders.size() <= 0) {
            return;
        }
        Example<ItxiaMember> example = Example.of(ItxiaMember.builder().acceptEmail(true).build());
        var listenList = itxiaMemberRepository.findAll(example);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("亲爱的IT侠，预约系统有 ");
        stringBuilder.append(orders.size());
        stringBuilder.append(" 份新的预约：<br /><br />");
        for (int i = 0; i < orders.size(); i++) {
            stringBuilder.append("  ");
            stringBuilder.append(i);
            stringBuilder.append(". [");
            stringBuilder.append(orders.get(i).getLocationRawValue());
            stringBuilder.append("] ");
            stringBuilder.append(orders.get(i).getProblemDescription());
            stringBuilder.append("<br /><br />");
        }
        stringBuilder.append("有时间请到预约系统查看~");
        mailService.sendMail("IT侠新预约提醒",
                stringBuilder.toString(),
                listenList.stream().map(ItxiaMember::getEmail).collect(Collectors.toList()));
    }

    /**
     * 上传某一周的维修记录
     *
     * @param week 周数
     * @return 是否成功
     */
    public WrapperResponse generateMaintenanceRecord(int year, int week) {
        Timestamp startTime = TimeUtil.someWeekStartTime(year, week);
        Timestamp endTime = TimeUtil.someWeekEndTime(year, week);
        List<Order> orders = orderRepository.findByLastEditTimeBetween(startTime, endTime);
        String key = "maintenance_log/year" + year + "_week" + week + ".txt";
        StringBuilder builder = new StringBuilder();
        orders.stream().map(this::formatOrder).forEach(builder::append);
        this.upload(key, builder.toString());
        return WrapperResponse.wrapSuccess();
    }

    /**
     * 将对象上传至阿里云OSS
     *
     * @param key     保存的key
     * @param content 保存的内容
     */
    private void upload(String key, String content) {
        OSSClient ossClient = new OSSClient(endPoint, accessKey, accessKeySecret);
        ossClient.putObject(bucketName, key, new ByteArrayInputStream(content.getBytes()));
        ossClient.shutdown();
    }

    /**
     * 根据维修单生成格式化的字符串
     *
     * @param order 维修单
     */
    private String formatOrder(Order order) {
        StringBuilder builder = new StringBuilder("姓名: ");
        builder.append(getFuzzedName(order.getCustomer()));
        builder.append("\r\n预约时间: ");
        builder.append(new DateTime(order.getLastEditTime()));
        builder.append("\r\n校区: ");
        builder.append(order.getLocation().getValue());
        builder.append("\r\n设备类型: ");
        builder.append(order.getDeviceModel());
        builder.append("\r\n操作系统: ");
        builder.append(order.getOsVersion());
        builder.append("\r\n问题描述: ");
        builder.append(order.getProblemDescription());
        builder.append("\r\n\r\n");
        return builder.toString();
    }

    /**
     * 给名字打*号
     * 规则是保留第一个，其余替换为*
     *
     * @param name 原来的客户名字
     * @return 打了*号的名字
     */
    private String getFuzzedName(String name) {
        if (name.length() < 2) {
            return "*";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(name.substring(0, 1));
        for (int i = 1; i < name.length(); i++) {
            builder.append("*");
        }
        return builder.toString();
    }
}
