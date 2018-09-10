package com.itxia.backend.controller;

import com.itxia.backend.controller.vo.AppointmentParam;
import com.itxia.backend.controller.vo.WrapperResponse;
import com.itxia.backend.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @RequestMapping("/appointment")
    public WrapperResponse makeAppointment(@RequestBody AppointmentParam appointmentParam, HttpServletRequest request) {
        String customerId = Optional.of(request).map(r -> r.getHeader("id")).orElse(null);
        return customerService.makeAppointment(customerId, appointmentParam);
    }

    @RequestMapping("/deleteAppointment")
    public WrapperResponse deleteAppointment(int orderId, HttpServletRequest request) {
        String customerId = Optional.of(request).map(r -> r.getHeader("id")).orElse(null);
        return customerService.deleteAppointment(customerId, orderId);
    }

    @RequestMapping("/getCurrentAppointment")
    public WrapperResponse getCurrentAppointment(HttpServletRequest request) {
        String customerId = Optional.of(request).map(r -> r.getHeader("id")).orElse(null);
        return customerService.getCurrentAppointment(customerId);
    }

    @RequestMapping("/modifyAppointment")
    public WrapperResponse modifyAppointment(@RequestBody AppointmentParam appointmentParam, HttpServletRequest
            request) {
        String customerId = Optional.of(request).map(r -> r.getHeader("id")).orElse(null);
        return customerService.modifyAppointment(customerId, appointmentParam);
    }

    @RequestMapping("/getAppointments")
    public WrapperResponse getAppointments(HttpServletRequest request) {
        String customerId = Optional.of(request).map(r -> r.getHeader("id")).orElse(null);
        return customerService.getAppointments(customerId);
    }
}
