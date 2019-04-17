package com.itxia.backend.aspect;

import com.itxia.backend.data.model.RequestMessage;
import com.itxia.backend.data.repo.RequestMessageRepository;
import com.itxia.backend.util.IpUtil;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @author Yzh
 * 对用户的请求相关信息进行记录，存至数据库
 */
@Aspect
@Component
public class LogAspect {

    private final RequestMessageRepository requestMessageRepository;

    @Autowired
    public LogAspect(RequestMessageRepository requestMessageRepository) {
        this.requestMessageRepository = requestMessageRepository;
    }

    @Pointcut("execution(public * com.itxia.backend.controller..*(..))")
    public void log() {
    }

    @Before("log()")
    public void logInfo() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        String uri = request.getRequestURI();
        String method = request.getMethod();
        String ip = IpUtil.getIpAddress(request);
        String time = new DateTime().toString();
        String params = getParams(request);
        RequestMessage message = RequestMessage.builder()
                .address(ip)
                .uri(uri)
                .method(method)
                .time(time)
                .params(params)
                .build();
        requestMessageRepository.save(message);
    }

    private String getParams(HttpServletRequest request) {
        Enumeration<String> params = request.getParameterNames();
        StringBuilder stringBuilder = new StringBuilder();
        while (params.hasMoreElements()) {
            String paramKey = params.nextElement();
            stringBuilder.append(paramKey);
            stringBuilder.append(":");
            stringBuilder.append(request.getParameter(paramKey));
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
