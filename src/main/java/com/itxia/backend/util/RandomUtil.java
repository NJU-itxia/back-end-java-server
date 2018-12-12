package com.itxia.backend.util;

import java.util.Random;

/**
 * @author Yzh
 */
public class RandomUtil {

    /**
     * 得到长度为length的验证码
     *
     * @param length 长度
     * @return 返回结果
     */
    public static String getRandomCode(int length) {
        Random random = new Random();
        return random.ints(length, 0, 10).boxed().map(String::valueOf)
                .reduce((a, b) -> a + b).orElse(null);
    }
}
