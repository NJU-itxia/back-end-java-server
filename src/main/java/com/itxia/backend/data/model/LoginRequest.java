package com.itxia.backend.data.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * @author Yzh
 * 用户登录记录
 *
 */
@Entity
@Builder
@Data
public class LoginRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 尝试的手机号
     */
    private String phone;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 验证码
     */
    private String code;

    /**
     * 发送验证码的时间
     */
    private LocalDateTime sendTime;

    /**
     * 实际登陆的时间
     * 如果没有说明登陆失败了或者没有登陆
     */
    private LocalDateTime loginTime;
}
