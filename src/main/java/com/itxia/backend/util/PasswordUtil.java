package com.itxia.backend.util;

import org.apache.commons.lang.StringUtils;
import org.jasypt.encryption.StringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 和用户密码相关的工具类
 * 预计功能如下：
 * - 检查密码是否符合规则
 * - 检查密码是否一致(好像没有必要？)
 * - 加密，解密密码(使用jasypt)
 * <p>
 * 加密和解密功能目前没有调用，需要等数据层分离之后再使用
 * <p>
 * 目前已实现功能：
 * - 检查密码是否符合规则
 * - 加密解密
 */
@Component
public class PasswordUtil {

    private static final Logger logger = LoggerFactory.getLogger(PasswordUtil.class);
    private static StringEncryptor stringEncryptor;

    /**
     * 配置从此处获得
     */
    @Resource
    private StaticConf configuration;

    /**
     * 为了注入静态属性
     */
    @PostConstruct
    private void init() {
        stringEncryptor = configuration.getStringEncryptor();
    }

    /**
     * 密码应该遵守如下规则：
     * 1. 长度8-16位
     * 2. 包含英文字母与数字
     * 3. 其他符号不做限制
     * 4. 必须以英文字母或数字开头(首位保留使用)
     *
     * @param password 被检查的密码
     * @return 返回结果
     */
    public static boolean isValidPassword(String password) {
        int length = StringUtils.length(password);
        if (length < 8 || length > 16) {
            logger.info("密码长度不在8-16区间");
            return false;
        }
        Pattern charAndNum = Pattern.compile("[^a-zA-Z0-9]");
        Matcher charAndNumMatcher = charAndNum.matcher(password.substring(0, 1));
        if (charAndNumMatcher.find()) {
            logger.info("密码不得以特殊字符开头");
            return false;
        }
        Pattern character = Pattern.compile("[a-zA-Z]");
        Matcher characterMatcher = character.matcher(password);
        if (!characterMatcher.find()) {
            logger.info("没有包含英文字母");
            return false;
        }
        Pattern number = Pattern.compile("[0-9]");
        Matcher numberMatcher = number.matcher(password);
        if (!numberMatcher.find()) {
            logger.info("没有包含数字");
            return false;
        }
        logger.info("密码符合规则");
        return true;
    }

    /**
     * 加密，加密后会打上前缀.
     * 不处理.开头的密码
     *
     * @param password 原密码
     * @return 加密结果
     */
    public static String encryptPassword(String password) {
        if (password.charAt(0) == '.') {
            return password;
        } else {
            return "." + stringEncryptor.encrypt(password);
        }
    }

    /**
     * 解密，去掉前缀itxia后解密
     * 如果没有前缀，说明不需要解密
     * 用来兼容原来的密码
     *
     * @param password 解密前密码
     * @return 解密结果
     */
    public static String decryptPassword(String password) {
        if (password.charAt(0) == '.') {
            return stringEncryptor.decrypt(password.substring(1));
        } else {
            return password;
        }
    }
}
