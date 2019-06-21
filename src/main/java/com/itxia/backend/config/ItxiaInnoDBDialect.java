package com.itxia.backend.config;

import org.hibernate.dialect.MySQL5InnoDBDialect;

/**
 * 为了让mysql 8以下的数据库默认使用utf8mb4.
 * 在properties里面配置dialect
 * @author zhenxi
 * */
public class ItxiaInnoDBDialect extends MySQL5InnoDBDialect {

    @Override
    public String getTableTypeString() {
        return "ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_unicode_ci";
    }
}
