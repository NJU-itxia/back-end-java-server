package com.itxia.backend.controller.vo;

import lombok.Builder;
import lombok.Data;

/**
 * 封装预约请求参数
 */
@Data
@Builder
public class AppointmentParam {

    /**
     * 预约的id
     */
    private String id;

    /**
     * 预约人的姓名
     */
    private String name;

    /**
     * 预约人的邮箱
     */
    private String email;

    /**
     * 预约人的校区
     */
    private String campus;

    /**
     * 设备型号
     */
    private String deviceVersion;

    /**
     * 设备的操作系统
     */
    private String systemVersion;

    /**
     * 问题描述
     */
    private String description;

    /**
     * 问题描述用的文件路径
     */
    private String file;
}
