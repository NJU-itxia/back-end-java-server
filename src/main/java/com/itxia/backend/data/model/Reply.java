package com.itxia.backend.data.model;

import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author Yzh
 * 预约单的评论/回复
 */
@Entity
@Table(name="reply")
@Data
@Builder
public class Reply {

    /**
     * 回复的id
     */
    @Id
    @Column(name = "`index`")
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
     * 回复的IT侠
     */
    @OneToOne
    @JoinColumn(name = "`ITXIAID`")
    @NotFound(action = NotFoundAction.IGNORE)
    private ItxiaMember itxia;

    /**
     * 回复时间
     */
    @Column(name = "time")
    private Timestamp replyTime;

    /**
     * 回复内容
     */
    private String content;

    public String getReplyTime() {
        DateTime dateTime = new DateTime(this.replyTime);
        dateTime.withZone(DateTimeZone.forOffsetHours(8));
        return dateTime.toString("yyyy-MM-dd HH:mm:ss");
    }

    public String getItxiaName() {
        if(itxia == null) {
            return "";
        }
        return itxia.getName();
    }

    private ItxiaMember getItxia() {
        return null;
    }
}
