package com.itxia.backend.data.model;

import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Yzh
 * 查询用的维修单实体
 */
@Entity
@Table(name = "`order`")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class OrderQuery {

    @Id
    @Getter
    private Integer id;

    @Getter
    @Column(name = "name")
    private String customer;

    @Column(name = "updatedon")
    private Timestamp dateTime;

    @OneToOne
    @JoinColumn(name = "handler")
    @NotFound(action = NotFoundAction.IGNORE)
    private ItxiaMember itxia;

    @Getter
    private String location;

    @Getter
    @Column(name = "os")
    private String osVersion;

    @Getter
    @Column(name = "model")
    private String deviceModel;

    @Getter
    @Column(name = "`desc`")
    private String problemDescription;

    @Getter
    private String email;

    @Getter
    private String phone;

    private Integer status;

    @Getter
    @OneToMany
    @JoinColumn(name = "`ORDERID`")
    private List<Reply> replyList;

    public String getTime() {
        DateTime dateTime = new DateTime(this.dateTime);
        dateTime.withZone(DateTimeZone.forOffsetHours(8));
        return dateTime.toString("yyyy-MM-dd HH:mm:ss");
    }

    public String getHandler() {
        if (itxia == null) {
            return null;
        }
        return itxia.getName();
    }

    public String getOrderStatus() {
        return Order.Status.fromIndex(status).getDescription();
    }
}
