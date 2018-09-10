package com.itxia.backend.data.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "`order`")
public class Order {

    /**
     * 订单id
     */
    @Id
    private Integer id;

    /**
     * 最后编辑时间
     */
    @Column(name = "updatedon")
    private Timestamp lastEditTime;

    /**
     * 预约人联系电话
     */
    private String phone;

    /**
     * 预约人邮箱
     */
    private String email;

    /**
     * 预约人校区
     */
    @Transient
    private Location location;

    /**
     * 从数据库取出的字符串
     * 不要使用
     */
    @Column(name = "location")
    private String locationRawValue;

    /**
     * 设备型号
     */
    @Column(name = "model")
    private String deviceModel;

    /**
     * 操作系统型号
     */
    @Column(name = "os")
    private String osVersion;

    /**
     * 问题描述
     */
    @Column(name = "desc")
    private String problemDescription;

    /**
     * 接单人的id
     */
    private Integer handler;

    @Column(name = "name")
    private String customer;

    /**
     * 订单状态
     */
    @Transient
    private Status status;

    /**
     * 从数据库去除的状态
     * 不要使用
     */
    @Column(name = "status")
    private Integer statusRawValue;

    /**
     * 当status为空时被调用
     * 传入statusRawValue，将status设置为仙林或鼓楼
     *
     * @param status 校区的字符串
     */
    private void setStatus(Integer status) {
        if (status == 0) {
            this.status = Status.CREATED;
        } else if (status == 1) {
            this.status = Status.ACCEPTED;
        } else if (status == 2) {
            this.status = Status.FINISHED;
        } else if (status == 3) {
            this.status = Status.NO_SOLUTION;
        } else if (status == 4) {
            this.status = Status.CANCELED;
        }
    }

    /**
     * 当location为空时被调用
     * 传入locationRawValue，将location设置为仙林或鼓楼
     *
     * @param location 校区的字符串
     */
    private void setLocation(String location) {
        if ("鼓楼".equals(location)) {
            this.location = Location.GU_LOU;
        } else if ("仙林".equals(location)) {
            this.location = Location.XIAN_LIN;
        } else {
            this.location = Location.UNDEFINED;
        }
    }

    public Location getLocation() {
        if (this.location == null) {
            setLocation(this.locationRawValue);
        }
        return location;
    }

    public Status getStatus() {
        if (this.status == null) {
            setStatus(this.statusRawValue);
        }
        return status;
    }

    /**
     * 订单的状态
     */
    private enum Status {
        FINISHED("已解决"), CREATED("新创建"), ACCEPTED("已接受"), NO_SOLUTION("无法解决"), CANCELED("用户取消"), UNDEFINED("未知");

        Status(String description) {
        }
    }
}
