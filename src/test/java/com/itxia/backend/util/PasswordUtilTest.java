package com.itxia.backend.util;

import lombok.var;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * 检查PasswordUtil
 * 工具类不能有问题 不能有问题
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class PasswordUtilTest {

    /**
     * 满足：
     * 1. 长度[8,16]
     * 2. 必须包含英文和数字
     * 3. 其他字符不做限制
     */
    @Test
    public void isValidPassword() {
        assertFalse("密码为null时，返回false", PasswordUtil.isValidPassword(null));
        assertFalse("密码为空时，返回false", PasswordUtil.isValidPassword(""));
        assertFalse("密码长度为7时，返回false", PasswordUtil.isValidPassword("123456a"));
        assertFalse("密码长度为17时，返回false", PasswordUtil.isValidPassword("123456789101112ab"));
        assertFalse("密码长度为7时，返回false", PasswordUtil.isValidPassword("123456a"));
        assertFalse("密码长度为8时，只有数字，返回false", PasswordUtil.isValidPassword("12345678"));
        assertFalse("密码长度为16时，只有数字，返回false", PasswordUtil.isValidPassword("1234567812345678"));
        assertFalse("密码长度为8时，只有字母，返回false", PasswordUtil.isValidPassword("absjdhri"));
        assertTrue("密码长度为8时，只有字母和数字，返回true", PasswordUtil.isValidPassword("absj1234"));
        assertTrue("密码长度为8时，包含字母、数字和其他字符，返回true", PasswordUtil.isValidPassword("a-sj1234"));
        assertTrue("密码长度为16时，只有字母和数字，返回true", PasswordUtil.isValidPassword("assj1234sdas8372"));
        assertTrue("密码长度为16时，包含字母、数字和其他字符，返回true", PasswordUtil.isValidPassword("assj1234sdas8372"));
    }

    /**
     * 输出一下加密的效果
     * 可能要加上一些，如.开头不加密（或特殊符号开头不加密）
     */
    @Test
    public void encryptPassword() {
        System.out.println(PasswordUtil.encryptPassword("12312123"));
        System.out.println(PasswordUtil.encryptPassword("asdjlsaj"));
        System.out.println(PasswordUtil.encryptPassword("asda1233s0s0s0s0"));
        System.out.println(PasswordUtil.encryptPassword("sdajiosdzzzz"));
    }

    /**
     * 检查能不能解密字符串
     * 1. 对某个字符串加密
     * 2. 对加密后的字符串解密
     * 3. 对比是否相等
     * <p>
     * 未加密的密码，使用解密方法应当没有效果
     * 即不以.开头的密码应该被无视
     * 1. 解密不以.开头的密码
     * 2. 检查是否不变
     */
    @Test
    public void decryptPassword() {
        var list = Arrays.asList("asdjlaksd", "sjkjd8798", "djaskld989783=", "jxizoll", "aaa", "sjxojixj");
        list.forEach(origin -> {
            String enc = PasswordUtil.encryptPassword(origin);
            String dec = PasswordUtil.decryptPassword(enc);
            assertEquals("加密解密后应相同。处理字符串: " + origin, origin, dec);
            dec = PasswordUtil.decryptPassword(origin);
            assertEquals("对未加密的密码，直接解密后应与原字符串相同。字符串: " + origin, origin, dec);
        });
    }
}