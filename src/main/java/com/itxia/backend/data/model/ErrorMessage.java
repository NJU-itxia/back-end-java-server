package com.itxia.backend.data.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

/**
 * @author Yzh
 */
@Entity
@Table(name = "errors")
@Data
@Builder
public class ErrorMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String os;

    private String browser;

    private String ip;

    private String time;

    private String message;
}
