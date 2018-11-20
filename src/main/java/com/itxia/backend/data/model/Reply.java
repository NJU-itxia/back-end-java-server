package com.itxia.backend.data.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 预约单的评论/回复
 */
@Entity
@Data
@Builder
public class Reply {

    /**
     * 回复的id
     */
    @Id
    @Column(name = "index")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 是否为it侠的回复
     */
    @Column(name = "replybool")
    private Boolean itxiaReply;

    /**
     * 对应的预约单的id
     */
    @Column(name = "`ORDERID`")
    private Integer orderId;

    /**
     * 回复的it侠的id
     */
    @Column(name = "`ITXIAID`")
    private Integer itxiaId;

    /**
     * 回复时间
     */
    @Column(name = "time")
    private Timestamp replyTime;

    /**
     * 回复内容
     */
    private String content;
}
