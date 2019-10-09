package com.itxia.backend.controller;

import com.itxia.backend.controller.vo.AddMemberParam;
import com.itxia.backend.controller.vo.SelfInfoParam;
import com.itxia.backend.controller.vo.WrapperResponse;
import com.itxia.backend.data.model.Location;
import com.itxia.backend.service.AdminOrderService;
import com.itxia.backend.service.AdminService;
import com.itxia.backend.service.KnightService;
import com.itxia.backend.service.MaintenanceRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static com.itxia.backend.controller.api.AdminControllerApiDescription.*;

/**
 * @author Yzh
 * 处理/admin的请求
 */
@RestController
@RequestMapping("/admin")
@Api(CLASS_API)
public class AdminController {

    private final KnightService knightService;

    private final AdminService adminService;

    private final AdminOrderService adminOrderService;

    private final MaintenanceRecordService maintenanceRecordService;

    @Autowired
    public AdminController(KnightService knightService, AdminService adminService, AdminOrderService adminOrderService,
                           MaintenanceRecordService maintenanceRecordService) {
        this.knightService = knightService;
        this.adminService = adminService;
        this.adminOrderService = adminOrderService;
        this.maintenanceRecordService = maintenanceRecordService;
    }

    @PostMapping("/info")
    public WrapperResponse loadInfo(HttpServletRequest request) {
        String knightId = Optional.of(request).map(r -> r.getHeader("id")).orElse(null);
        return knightService.getSelfInfo(knightId);
    }

    @ApiOperation(value = UPDATE_SELF_INFO)
    @PostMapping("/updateInfo")
    public WrapperResponse updateInfo(@RequestBody SelfInfoParam selfInfoParam,
                                      HttpServletRequest request) {
        String knightId = Optional.of(request).map(r -> r.getHeader("id")).orElse(null);
        return knightService.updateSelfInfo(knightId,selfInfoParam);
    }

    @ApiOperation(value = METHOD_MOD_PWD, notes = NOTES_MOD_PWD)
    @PostMapping("/password/modify")
    public WrapperResponse modifyPassword(@ApiParam(value = PARAM_MOD_PWD_OLD_PWD) String oldPassword,
                                          @ApiParam(value = PARAM_MOD_PWD_NEW_PWD) String newPassword,
                                          HttpServletRequest request) {
        String knightId = Optional.of(request).map(r -> r.getHeader("id")).orElse(null);
        return knightService.modifyPassword(knightId, oldPassword, newPassword);
    }

    @ApiOperation(value = METHOD_APM_ALL, notes = NOTES_APM_ALL)
    @PostMapping("/appointment/all")
    public WrapperResponse listAppointments() {
        return adminService.listAllOrder();
    }

    @ApiOperation(value = METHOD_APM_BY_PAGE)
    @PostMapping("/appointment/page/{page}/size/{size}")
    public WrapperResponse listAppointmentByPage(
            @ApiParam(value = PARAM_APM_BY_PAGE_PAGE) @PathVariable("page") Integer pageNum,
            @ApiParam(value = PARAM_APM_BY_PAGE_SIZE) @PathVariable("size") Integer pageSize) {
        return adminService.listOrderBy(pageNum, pageSize);
    }


    @ApiOperation(value = METHOD_APM_NUM, notes = NOTES_APM_NUM)
    @PostMapping("/appointment/location/{location}/search/{search}")
    public WrapperResponse getAppointmentNum(
            @ApiParam(value = PARAM_APM_NUM_LOC) @PathVariable("location") String location,
            @ApiParam(value = "") @PathVariable("search") String search) {
        return adminOrderService.getSearchNumber(location, search);
    }

    @ApiOperation(value = METHOD_APM_BY_COND)
    @PostMapping("/appointment/location/{location}/state/{state}/search/{search}/page/{page}/size/{size}")
    public WrapperResponse listAppointmentByCondition(
            @ApiParam(value = PARAM_APM_BY_COND_LOC) @PathVariable("location") String location,
            @ApiParam(value = PARAM_APM_BY_COND_STATE) @PathVariable("state") String state,
            @ApiParam(value = PARAM_APM_BY_COND_SEARCH) @PathVariable("search") String search,
            @ApiParam(value = PARAM_APM_BY_COND_PAGE) @PathVariable("page") Integer pageNum,
            @ApiParam(value = PARAM_APM_BY_COND_SIZE) @PathVariable("size") Integer pageSize,
            HttpServletRequest request) {
        String knightId = Optional.of(request).map(r -> r.getHeader("id")).orElse(null);
        return adminOrderService.searchOrder(location, state, search, pageNum, pageSize, knightId);
    }

    @ApiOperation(value = METHOD_ACT_APM, notes = NOTES_ACT_APM)
    @PostMapping("/appointment/accept/{id}")
    public WrapperResponse acceptAppointment(@ApiParam(value = PARAM_ACT_APM_ID) @PathVariable("id") Integer appointmentId,
                                             HttpServletRequest request) {
        String knightId = Optional.of(request).map(r -> r.getHeader("id")).orElse(null);
        return knightService.acceptAppointment(knightId, appointmentId);
    }

    @ApiOperation(value = "", notes = "")
    @PostMapping("/appointment/cancel/{id}")
    public WrapperResponse putBackAppointment(@ApiParam(value = "") @PathVariable("id") Integer appointmentId,
                                             HttpServletRequest request) {
        String knightId = Optional.of(request).map(r -> r.getHeader("id")).orElse(null);
        return knightService.putBackAppointment(knightId, appointmentId);
    }

    @PostMapping("/appointment/finish/{id}")
    public WrapperResponse finishAppointment(@ApiParam(value = PARAM_ACT_APM_ID) @PathVariable("id") Integer appointmentId,
                                             HttpServletRequest request) {
        String knightId = Optional.of(request).map(r -> r.getHeader("id")).orElse(null);
        return knightService.finishAppointment(knightId, appointmentId);
    }

    @PutMapping("/appointment/reply/{oid}/{content}")
    @ApiOperation(value = METHOD_REPLY, notes = NOTES_REPLY)
    public WrapperResponse reply(@ApiParam(value = PARAM_REPLY_OID) @PathVariable("oid") Integer appointmentId,
                                 @ApiParam(value = PARAM_REPLY_CONTENT) @PathVariable("content") String content,
                                 HttpServletRequest request) {
        String knightId = Optional.of(request).map(r -> r.getHeader("id")).orElse(null);
        return knightService.reply(knightId, appointmentId, content);
    }

    @ApiOperation(value = METHOD_CREATE_MBR, notes = NOTES_CREATE_MBR)
    @PutMapping("/member/create")
    public WrapperResponse createMember(@RequestBody AddMemberParam addMemberParam) {
        return adminService.createMember(addMemberParam.getLoginName(), addMemberParam.getPassword(), Location.fromValue(addMemberParam.getLocation()), addMemberParam.getName(),addMemberParam.getEmail(),addMemberParam.getAcceptEmail());
    }

    @ApiOperation(value = METHOD_MBR_ALL, notes = NOTES_MBR_ALL)
    @PostMapping("/member/all")
    public WrapperResponse listAllMembers(String id) {
        return adminService.listAllMembers();
    }

    @ApiOperation(value = METHOD_MOD_MBR_PWD, notes = NOTES_MOD_MBR_PWD)
    @PostMapping("/password/modify/{member}")
    public WrapperResponse modifyMemberPassword(@ApiParam(value = PARAM_MOD_MBR_PWD_USER) @PathVariable("member") String memberId,
                                                @ApiParam(value = PARAM_MOD_MBR_PWD_PWD) String newPassword) {
        return adminService.modifyMemberPassword(memberId, newPassword);
    }

    @ApiOperation(value = METHOD_RCD_UPLOAD, notes = NOTES_RCD_UPLOAD)
    @PostMapping("/maintenance/upload/{year}/{week}")
    public WrapperResponse uploadMaintenanceRecord(
            @PathVariable("year") Integer year,
            @ApiParam(value = PARAM_RCD_UPLOAD_WEEK) @PathVariable("week") Integer week
    ) {
        return maintenanceRecordService.generateMaintenanceRecord(year, week);
    }
}
