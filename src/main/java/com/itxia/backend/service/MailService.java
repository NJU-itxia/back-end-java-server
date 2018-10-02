package com.itxia.backend.service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.dm.model.v20151123.SingleSendMailResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.itxia.backend.controller.vo.WrapperResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 邮件服务，后面要改动，添加接口主要用于测试
 */
@Service
@Transactional
public class MailService {

    /**
     * access key
     */
    @Value("${aliyun.key}")
    private String mailAccessKey;

    /**
     * access key secret
     */
    @Value("${aliyun.secret}")
    private String mailAccessKeySecret;

    /**
     * 发送方的地址，如xxx@mail.yyy.com
     */
    @Value("${aliyun.mail.address}")
    private String sendAddress;

    /**
     * 发送者的名字，如IT侠
     */
    @Value("${aliyun.mail.name}")
    private String sendName;

    /**
     * 邮件的标签
     */
    @Value("${aliyun.mail.tag}")
    private String tag;

    /**
     * 发送邮件
     *
     * @param title       邮件标题
     * @param content     邮件内容
     * @param addressList 收邮件地址的列表
     * @return 返回操作结果
     */
    public WrapperResponse sendMail(String title, String content, List<String> addressList) {
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", mailAccessKey, mailAccessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        SingleSendMailRequest request = new SingleSendMailRequest();
        try {
            request.setAccountName(sendAddress);
            request.setFromAlias(sendName);
            request.setAddressType(1);
            request.setTagName(tag);
            request.setReplyToAddress(true);
            String toAddress = addressList.stream().reduce((a, b) -> a + ',' + b).orElse("");
            request.setToAddress(toAddress);
            request.setSubject(title);
            request.setHtmlBody(content);
            SingleSendMailResponse httpResponse = client.getAcsResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return WrapperResponse.wrapSuccess();
    }
}
