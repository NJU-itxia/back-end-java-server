package com.itxia.backend.util;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 检查PasswordUtil
 * 工具类不能有问题 不能有问题
 */
public class PasswordUtilTest {

    /**
     * 满足：
     * 1. 长度[8,16]
     * 2. 必须包含英文和数字
     * 3. 其他字符不做限制
     */
    @Test
    public void isValidPassword() {
        Assert.assertFalse("密码为null时，返回false", PasswordUtil.isValidPassword(null));
        Assert.assertFalse("密码为空时，返回false", PasswordUtil.isValidPassword(""));
        Assert.assertFalse("密码长度为7时，返回false", PasswordUtil.isValidPassword("123456a"));
        Assert.assertFalse("密码长度为17时，返回false", PasswordUtil.isValidPassword("123456789101112ab"));
        Assert.assertFalse("密码长度为7时，返回false", PasswordUtil.isValidPassword("123456a"));
        Assert.assertFalse("密码长度为8时，只有数字，返回false", PasswordUtil.isValidPassword("12345678"));
        Assert.assertFalse("密码长度为16时，只有数字，返回false", PasswordUtil.isValidPassword("1234567812345678"));
        Assert.assertFalse("密码长度为8时，只有字母，返回false", PasswordUtil.isValidPassword("absjdhri"));
        Assert.assertTrue("密码长度为8时，只有字母和数字，返回true", PasswordUtil.isValidPassword("absj1234"));
        Assert.assertTrue("密码长度为8时，包含字母、数字和其他字符，返回true", PasswordUtil.isValidPassword("a-sj1234"));
        Assert.assertTrue("密码长度为16时，只有字母和数字，返回true", PasswordUtil.isValidPassword("assj1234sdas8372"));
        Assert.assertTrue("密码长度为16时，包含字母、数字和其他字符，返回true", PasswordUtil.isValidPassword("assj1234sdas8372"));
    }
}