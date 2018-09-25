package com.itxia.backend.util;

import lombok.Getter;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 静态类的配置从这里获取
 * 目前只有一个StringEncryptor
 */
@Configuration
@Component
@Getter
public class StaticConf {

    private final StringEncryptor stringEncryptor;

    @Autowired
    public StaticConf(StringEncryptor stringEncryptor) {
        this.stringEncryptor = stringEncryptor;
    }
}
