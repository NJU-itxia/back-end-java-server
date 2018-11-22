package com.itxia.backend.data.model;

import lombok.Getter;

/**
 * @author Yzh
 * 校区的enum
 * 仙林，或者鼓楼
 */
@Getter
public enum Location {

    /**
     * 仙林校区
     */
    XIAN_LIN("仙林"),

    /**
     * 鼓楼校区
     */
    GU_LOU("鼓楼"),

    /**
     * 代表其他情况
     */
    UNDEFINED("未知");

    private String value;

    Location(String value) {
        this.value = value;
    }

    public static Location fromValue(String value) {
        final String xianLinValue = "仙林";
        final String guLouValue = "鼓楼";
        if (xianLinValue.equals(value)) {
            return Location.XIAN_LIN;
        } else if (guLouValue.equals(value)) {
            return Location.GU_LOU;
        } else {
            return Location.UNDEFINED;
        }
    }
}