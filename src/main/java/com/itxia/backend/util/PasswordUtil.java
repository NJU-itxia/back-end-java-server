package com.itxia.backend.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 和用户密码相关的工具类
 * 预计功能如下：
 * - 检查密码是否符合规则
 * - 检查密码是否一致
 * - 加密，解密密码
 * <p>
 * 目前已实现功能：
 * - 检查密码是否符合规则
 */
@UtilityClass
public class PasswordUtil {

    private static final Logger logger = LoggerFactory.getLogger(PasswordUtil.class);

    /**
     * 密码应该遵守如下规则：
     * 1. 长度8-16位
     * 2. 包含英文字母与数字
     * 3. 其他符号不做限制
     *
     * @param password 被检查的密码
     * @return 返回结果
     */
    public boolean isValidPassword(String password) {
        int length = StringUtils.length(password);
        if (length < 8 || length > 16) {
            logger.info("密码长度不在8-16区间");
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
}
