package com.itxia.backend.controller;

import com.itxia.backend.controller.vo.AppointmentParam;
import com.itxia.backend.controller.vo.WrapperResponse;
import com.itxia.backend.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 处理/customer的请求
 * 无需改动
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

    @PostMapping("/appointment")
    public WrapperResponse makeAppointment(@RequestBody AppointmentParam appointmentParam, HttpServletRequest request) {
        logger.info("/customer/appointment");
        String customerId = Optional.of(request).map(r -> r.getHeader("id")).orElse(null);
        return customerService.makeAppointment(customerId, appointmentParam);
    }

    @PostMapping("/deleteAppointment")
    public WrapperResponse deleteAppointment(int orderId, HttpServletRequest request) {
        String customerId = Optional.of(request).map(r -> r.getHeader("id")).orElse(null);
        return customerService.deleteAppointment(customerId, orderId);
    }

    @PostMapping("/getCurrentAppointment")
    public WrapperResponse getCurrentAppointment(HttpServletRequest request) {
        String customerId = Optional.of(request).map(r -> r.getHeader("id")).orElse(null);
        return customerService.getCurrentAppointment(customerId);
    }

    @PostMapping("/modifyAppointment")
    public WrapperResponse modifyAppointment(@RequestBody AppointmentParam appointmentParam, HttpServletRequest
            request) {
        String customerId = Optional.of(request).map(r -> r.getHeader("id")).orElse(null);
        return customerService.modifyAppointment(customerId, appointmentParam);
    }

    @PostMapping("/getAppointments")
    public WrapperResponse getAppointments(HttpServletRequest request) {
        String customerId = Optional.of(request).map(r -> r.getHeader("id")).orElse(null);
        return customerService.getAppointments(customerId);
    }

    @PostMapping("/reply")
    public WrapperResponse reply(int appointmentId, String content, HttpServletRequest request) {
        String customerId = Optional.of(request).map(r -> r.getHeader("id")).orElse(null);
        return customerService.commentOnAppointment(customerId, appointmentId, content);
    }
}
