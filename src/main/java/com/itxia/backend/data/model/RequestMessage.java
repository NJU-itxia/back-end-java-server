package com.itxia.backend.data.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

/**
 * @author Yzh
 */
@Entity
@Table(name = "requests")
@Data
@Builder
public class RequestMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String uri;

    private String method;

    private String address;

    private String time;

    private String params;
}
