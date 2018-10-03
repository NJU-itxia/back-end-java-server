package com.itxia.backend.data.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Builder
@Table(name = "`order`")
public class Order {

    /**
     * 订单id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Column(name = "`desc`")
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
     * 最后一个set是在创建的时候使用的，这个设计非常差劲，正在想办法修改
     *
     * @param status 校区的字符串
     */
    public void setStatus(Integer status) {
        if (status == Status.CREATED.getIndex()) {
            this.status = Status.CREATED;
        } else if (status == Status.ACCEPTED.getIndex()) {
            this.status = Status.ACCEPTED;
        } else if (status == Status.FINISHED.getIndex()) {
            this.status = Status.FINISHED;
        } else if (status == Status.NO_SOLUTION.getIndex()) {
            this.status = Status.NO_SOLUTION;
        } else if (status == Status.CANCELED.getIndex()) {
            this.status = Status.CANCELED;
        }
        this.statusRawValue = status;
    }

    /**
     * 当location为空时被调用
     * 传入locationRawValue，将location设置为仙林或鼓楼
     * 最后一个set是在创建的时候使用的，这个设计非常差劲，正在想办法修改
     *
     * @param location 校区的字符串
     */
    public void setLocation(String location) {
        if ("鼓楼".equals(location)) {
            this.location = Location.GU_LOU;
        } else if ("仙林".equals(location)) {
            this.location = Location.XIAN_LIN;
        } else {
            this.location = Location.UNDEFINED;
        }
        this.locationRawValue = location;
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
    @Getter
    public enum Status {
        FINISHED("已解决", 2), CREATED("新创建", 0), ACCEPTED("已接受", 1), NO_SOLUTION("无法解决", 3), CANCELED("用户取消", 4), UNDEFINED("未知", -1);

        private String description;

        private int index;

        Status(String description, int index) {
            this.description = description;
            this.index = index;
        }
    }
}
