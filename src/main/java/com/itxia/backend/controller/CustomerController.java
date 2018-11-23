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

import static com.itxia.backend.controller.api.CustomerControllerApiDescription.*;

/**
 * @author Yzh
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
    @ApiOperation(value = METHOD_MAKE_APM, notes = NOTES_MAKE_APM)
    public WrapperResponse makeAppointment(@ApiParam(value = PARAM_MAKE_APM) @RequestBody AppointmentParam appointmentParam,
                                           HttpServletRequest request) {
        logger.info("/customer/appointment");
        String customerId = Optional.of(request).map(r -> r.getHeader("id")).orElse(null);
        return customerService.makeAppointment(customerId, appointmentParam);
    }

    @Deprecated
    @DeleteMapping("/appointment/{appointment}")
    @ApiOperation(value = METHOD_CANCEL_APM, notes = NOTES_CANCEL_APM)
    public WrapperResponse deleteAppointment(@ApiParam(value = PARAM_CANCEL_APM_ID) @PathVariable("appointment") Integer orderId,
                                             HttpServletRequest request) {
        String customerId = Optional.of(request).map(r -> r.getHeader("id")).orElse(null);
        return customerService.deleteAppointment(customerId, orderId);
    }

    @DeleteMapping("/appointment/current")
    @ApiOperation(value = METHOD_CANCEL_CUR_APM, notes = NOTES_CANCEL_CUR_APM)
    public WrapperResponse deleteCurrentAppointment(HttpServletRequest request) {
        String customerId = Optional.of(request).map(r -> r.getHeader("id")).orElse(null);
        return customerService.deleteCurrentAppointment(customerId);
    }

    @PostMapping("/appointment/current")
    @ApiOperation(value = METHOD_CUR_APM, notes = NOTES_CUR_APM)
    public WrapperResponse getCurrentAppointment(HttpServletRequest request) {
        String customerId = Optional.of(request).map(r -> r.getHeader("id")).orElse(null);
        return customerService.getCurrentAppointment(customerId);
    }

    @PostMapping("/appointment/update")
    @ApiOperation(value = METHOD_MOD_APM, notes = NOTES_MOD_APM)
    public WrapperResponse modifyAppointment(@ApiParam(value = PARAM_MOD_APM_CTN) @RequestBody AppointmentParam appointmentParam,
                                             HttpServletRequest request) {
        String customerId = Optional.of(request).map(r -> r.getHeader("id")).orElse(null);
        return customerService.modifyAppointment(customerId, appointmentParam);
    }

    @PostMapping("/appointment/all")
    @ApiOperation(value = METHOD_ALL_APM, notes = NOTES_ALL_APM)
    public WrapperResponse getAppointments(HttpServletRequest request) {
        String customerId = Optional.of(request).map(r -> r.getHeader("id")).orElse(null);
        return customerService.getAppointments(customerId);
    }

    @PutMapping("/appointment/reply/{id}/{content}")
    @ApiOperation(value = METHOD_REPLY, notes = NOTES_REPLY)
    public WrapperResponse reply(@ApiParam(value = PARAM_REPLY_ID) @PathVariable("id") Integer appointmentId,
                                 @ApiParam(value = PARAM_REPLY_CTN) @PathVariable("content") String content,
                                 HttpServletRequest request) {
        String customerId = Optional.of(request).map(r -> r.getHeader("id")).orElse(null);
        return customerService.commentOnAppointment(customerId, appointmentId, content);
    }
}
