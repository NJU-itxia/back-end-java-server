package com.itxia.backend.data.model;

import lombok.Getter;

/**
 * 校区的enum
 * 仙林，或者鼓楼
 */
@Getter
public enum Location {
    XIAN_LIN("仙林"), GU_LOU("鼓楼"), UNDEFINED("未知");

    private String value;

    Location(String value) {
        this.value = value;
    }
}
