package com.itxia.backend.controller.vo;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Builder;
import lombok.Data;

/**
 * @author zzx
 * 封装更新个人信息的参数
 * 从member表中去除了id, password, admin字段
 */
@Data
@Builder
public class AddMemberParam {

    /**
     * 姓名
     * */
    private String name;

    /**
     * 登陆账号
     */
    private String loginName;

    /**
     * 密码
     */
    private String password;

    /**
     * 校区
     */
    private String location;

    /**
     * 是否接受邮件提醒
     */
    private Boolean acceptEmail;

    /**
     * 邮箱
     */
    private String email;
}
