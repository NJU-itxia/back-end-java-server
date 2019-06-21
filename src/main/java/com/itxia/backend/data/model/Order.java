package com.itxia.backend.data.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author Yzh
 */
@Entity
@Data
@Builder
@Table(name = "`order`")
public class Order {

    /**
     * 订单id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
        this.location = Location.fromValue(location);
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
        /**
         * 已解决
         */
        FINISHED("已解决", 2),

        /**
         * 新创建
         */
        CREATED("新创建", 0),

        /**
         * 已接单的维修单
         */
        ACCEPTED("已接受", 1),

        /**
         * 无法解决的维修单
         */
        NO_SOLUTION("无法解决", 3),

        /**
         * 由用户取消的维修单
         */
        CANCELED("用户取消", 4),

        /**
         * 其他情况使用
         */
        UNDEFINED("未知", -1);

        private String description;

        private int index;

        public static Status fromIndex(int i) {
            switch (i) {
                case -1:
                    return Status.UNDEFINED;
                case 0:
                    return Status.CREATED;
                case 1:
                    return Status.ACCEPTED;
                case 2:
                    return Status.FINISHED;
                case 3:
                    return Status.NO_SOLUTION;
                case 4:
                    return Status.CANCELED;
                default:
                    return Status.UNDEFINED;
            }
        }

        Status(String description, int index) {
            this.description = description;
            this.index = index;
        }

        /**
         * 是否为未完成的状态
         *
         * @return 返回结果
         */
        public boolean isUnfinished() {
            return this == Status.CREATED || this == Status.ACCEPTED;
        }
    }
}
