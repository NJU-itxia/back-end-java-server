package com.itxia.backend.controller;

import com.itxia.backend.controller.vo.WrapperResponse;
import com.itxia.backend.service.MailService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * 邮件服务的controller
 */
@RestController
@RequestMapping("/mail")
public class MailController {

    private final MailService mailService;

    @Autowired
    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    /**
     * 此方法传list有问题，尚未解决，可是使用request body
     *
     * @param title       标题
     * @param content     内容
     * @param addressList 列表
     * @return 结果
     */
    @PostMapping("/send")
    @ApiOperation(value = "暂停使用")
    public WrapperResponse sendMail(String title, String content, ArrayList<String> addressList) {
        return mailService.sendMail(title, content, addressList);
    }
}
