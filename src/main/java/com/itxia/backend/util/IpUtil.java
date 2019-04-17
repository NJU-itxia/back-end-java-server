package com.itxia.backend.util;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author Yzh
 */
public class IpUtil {

    /**
     * 传入request获取真实用户ip
     * 按顺序获取头部信息并判断有效性
     *
     * @param request 入参
     * @return 请求ip
     */
    public static String getIpAddress(HttpServletRequest request) {
        if (request == null) {
            return "";
        }
        List<String> values = Arrays.asList("X-FORWARDED-FOR",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_CLIENT_IP",
                "X-Real-IP");
        Iterator<String> iterator = values.iterator();
        String ip = "";
        String unknown = "unknown";
        while ((StringUtils.isEmpty(ip) || ip.equals(unknown)) && iterator.hasNext()) {
            ip = request.getHeader(iterator.next());
        }
        if ((StringUtils.isEmpty(ip) || ip.equals(unknown))) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
