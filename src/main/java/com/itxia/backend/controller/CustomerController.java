package com.itxia.backend.controller;

import com.itxia.backend.controller.vo.AppointmentParam;
import com.itxia.backend.controller.vo.WrapperResponse;
import com.itxia.backend.service.CustomerService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 处理/customer的请求
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PutMapping("/appointment")
    @ApiOperation(value = "用户添加一个预约", notes = "用户同时应该只能有一个状态为未完成的预约单")
    public WrapperResponse makeAppointment(@ApiParam(value = "预约单的内容") @RequestBody AppointmentParam appointmentParam,
                                           HttpServletRequest request) {
        logger.info("/customer/appointment");
        String customerId = Optional.of(request).map(r -> r.getHeader("id")).orElse(null);
        return customerService.makeAppointment(customerId, appointmentParam);
    }

    @DeleteMapping("/appointment/{appointment}")
    @ApiOperation(value = "用户取消一个预约", notes = "取消的预约单还在所有订单当中，只是状态标记为取消")
    public WrapperResponse deleteAppointment(@ApiParam(value = "预约单的id") @PathVariable("appointment") Integer orderId,
                                             HttpServletRequest request) {
        String customerId = Optional.of(request).map(r -> r.getHeader("id")).orElse(null);
        return customerService.deleteAppointment(customerId, orderId);
    }

    @PostMapping("/appointment/current")
    @ApiOperation(value = "用户获取当前未完成的预约单", notes = "在header中传入用户的id")
    public WrapperResponse getCurrentAppointment(HttpServletRequest request) {
        String customerId = Optional.of(request).map(r -> r.getHeader("id")).orElse(null);
        return customerService.getCurrentAppointment(customerId);
    }

    @PostMapping("/appointment/update")
    @ApiOperation(value = "用户修改当前未完成的预约单", notes = "在header中传入用户的id")
    public WrapperResponse modifyAppointment(@ApiParam(value = "预约单的内容") @RequestBody AppointmentParam appointmentParam,
                                             HttpServletRequest request) {
        String customerId = Optional.of(request).map(r -> r.getHeader("id")).orElse(null);
        return customerService.modifyAppointment(customerId, appointmentParam);
    }

    @PostMapping("/appointment/all")
    @ApiOperation(value = "用户获取自己的所有预约单", notes = "在header中传入用户的id")
    public WrapperResponse getAppointments(HttpServletRequest request) {
        String customerId = Optional.of(request).map(r -> r.getHeader("id")).orElse(null);
        return customerService.getAppointments(customerId);
    }

    @PutMapping("/appointment/reply/{id}/{content}")
    @ApiOperation(value = "用户给预约单添加一些评论回复", notes = "在header中传入用户的id")
    public WrapperResponse reply(@ApiParam(value = "预约单的id") @PathVariable("id") Integer appointmentId,
                                 @ApiParam(value = "具体回复内容") @PathVariable("content") String content,
                                 HttpServletRequest request) {
        String customerId = Optional.of(request).map(r -> r.getHeader("id")).orElse(null);
        return customerService.commentOnAppointment(customerId, appointmentId, content);
    }
}
