package com.itxia.backend.util;

import lombok.Getter;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 在Util中使用properties里面的配置，从这个类中获取
 * StaticConf负责将properties里面的配置读入
 * 目前只有一个StringEncryptor需要这个操作
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
