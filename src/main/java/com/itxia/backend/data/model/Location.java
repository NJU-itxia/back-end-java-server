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

    public static Location fromValue(String value) {
        if ("仙林".equals(value)) {
            return Location.XIAN_LIN;
        } else if ("鼓楼".equals(value)) {
            return Location.GU_LOU;
        } else {
            return Location.UNDEFINED;
        }
    }
}