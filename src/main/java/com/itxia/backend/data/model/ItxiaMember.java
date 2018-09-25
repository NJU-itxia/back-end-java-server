package com.itxia.backend.data.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

/**
 * IT侠成员model
 */
@Entity
@Table(name = "members")
@Data
@Builder
public class ItxiaMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 登陆账号
     */
    @Column(name = "account")
    private String loginName;

    /**
     * 密码
     */
    private String password;

    /**
     * 校区
     */
    @Transient
    private Location location;

    /**
     * 从数据库取出的原始值
     */
    @Column(name = "location")
    private String locationRawValue;

    /**
     * 是否为管理员
     */
    private Boolean admin;

    /**
     * 邮箱账号
     */
    private String email;

    /**
     * 考虑在此处处理解密
     * 获取到的应该是解密的密码
     * 在独立数据层之前不要使用
     *
     * @return 返回解密结果
     */
    public String getPassword() {
        return password;
    }

    /**
     * 考虑在此处处理加密
     * 设置进去的应该是加密后的密码
     * 在独立数据层之前不要使用
     *
     * @param password 原密码
     */
    public void setPassword(String password) {
        this.password = password;
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
}
