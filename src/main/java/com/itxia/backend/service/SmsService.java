package com.itxia.backend.service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.itxia.backend.controller.vo.WrapperResponse;
import com.itxia.backend.data.model.LoginRequest;
import com.itxia.backend.data.repo.LoginRequestRepository;
import com.itxia.backend.util.RandomUtil;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author Yzh
 */
@Service
@Transactional(rollbackFor = Throwable.class)
public class SmsService {

    private final LoginRequestRepository loginRequestRepository;

    @Value("${aliyun.key}")
    private String aliYunAccessKey;

    @Value("${aliyun.secret}")
    private String aliYunAccessKeySecret;

    @Autowired
    public SmsService(LoginRequestRepository loginRequestRepository) {
        this.loginRequestRepository = loginRequestRepository;
    }

    /**
     * 任务：
     * <p>
     * 从数据库里查找最近一分钟是否有过请求
     * 有，返回失败
     * 向用户发送短信，并向数据库添加记录
     *
     * @param phone 用户手机号码
     * @return 返回结果
     */
    public WrapperResponse requestLogin(String phone) {
        LocalDateTime lastMinute = LocalDateTime.now().minusMinutes(1);
        var request = loginRequestRepository.findOneAfterTimeByPhone(phone, lastMinute);
        if (request != null) {
            return WrapperResponse.wrapFail();
        }
        String randomCode = RandomUtil.getRandomCode(6);
        boolean success = sendSms(phone, randomCode);
        if (!success) {
            return WrapperResponse.wrapFail();
        }
        LocalDateTime now = LocalDateTime.now();
        LoginRequest loginRequest = LoginRequest.builder()
                .sendTime(now)
                .phone(phone)
                .code(randomCode)
                .build();
        loginRequestRepository.save(loginRequest);
        return WrapperResponse.wrapSuccess();
    }

    /**
     * 任务
     * <p>
     * 检查一分钟内的请求记录
     * 如果没有记录，返回失败
     * 如果有，检查验证码是否一致
     * 是，更新登陆请求记录，返回成功
     * 否则返回失败
     *
     * @param phone 用户手机号码
     * @param code  验证码
     * @return 返回结果
     */
    public WrapperResponse loginWithCode(String phone, String code) {
        LocalDateTime lastMinute = LocalDateTime.now().minusMinutes(1);
        var request = loginRequestRepository.findOneAfterTimeByPhone(phone, lastMinute);
        if (request == null) {
            return WrapperResponse.wrapFail();
        }
        if (request.getCode().equals(code)) {
            LocalDateTime now = LocalDateTime.now();
            request.setLoginTime(now);
            loginRequestRepository.save(request);
            return WrapperResponse.wrapSuccess();
        } else {
            return WrapperResponse.wrapFail();
        }
    }

    private boolean sendSms(String phone, String code) {
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        final String product = "Dysmsapi";
        final String domain = "dysmsapi.aliyuncs.com";
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", aliYunAccessKey,
                aliYunAccessKeySecret);
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        IAcsClient acsClient = new DefaultAcsClient(profile);
        SendSmsRequest request = new SendSmsRequest();
        request.setMethod(MethodType.POST);
        request.setPhoneNumbers(phone);
        request.setSignName("IT侠");
        request.setTemplateCode("SMS_149095858");
        request.setTemplateParam("{\"code\":\"" + code + "\"}");
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
            final String okString = "OK";
            if (sendSmsResponse.getCode() != null && okString.equals(sendSmsResponse.getCode())) {
                return true;
            }
            return false;
        } catch (ClientException e) {
            e.printStackTrace();
            return false;
        }
    }
}
