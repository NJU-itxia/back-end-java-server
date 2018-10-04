package com.itxia.backend.controller;

import com.itxia.backend.controller.vo.WrapperResponse;
import com.itxia.backend.data.model.Location;
import com.itxia.backend.service.AdminService;
import com.itxia.backend.service.KnightService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 处理/admin的请求
 * 无需改动
 */
@RestController
@RequestMapping("/admin")
@Api("IT侠后台用户使用的api")
public class AdminController {

    private final KnightService knightService;

    private final AdminService adminService;

    @Autowired
    public AdminController(KnightService knightService, AdminService adminService) {
        this.knightService = knightService;
        this.adminService = adminService;
    }

    @ApiOperation(value = "后台用户修改自己密码")
    @PostMapping("/modifyPassword")
    public WrapperResponse modifyPassword(String oldPassword, String newPassword, HttpServletRequest request) {
        String knightId = Optional.of(request).map(r -> r.getHeader("id")).orElse(null);
        return knightService.modifyPassword(knightId, oldPassword, newPassword);
    }

    @ApiOperation(value = "后台用户查看所有的预约单")
    @PostMapping("/listAppointments")
    public WrapperResponse listAppointments() {
        return adminService.listAllOrder();
    }

    @ApiOperation(value = "后台用户接单")
    @PostMapping("/acceptAppointment")
    public WrapperResponse acceptAppointment(int appointmentId, HttpServletRequest request) {
        String knightId = Optional.of(request).map(r -> r.getHeader("id")).orElse(null);
        return knightService.acceptAppointment(knightId, appointmentId);
    }

    @ApiOperation(value = "后台用户对某一个预约单添加评论回复")
    @PostMapping("/reply")
    public WrapperResponse reply(int appointmentId, String content, HttpServletRequest request) {
        String knightId = Optional.of(request).map(r -> r.getHeader("id")).orElse(null);
        return knightService.reply(knightId, appointmentId, content);
    }

    @ApiOperation(value = "管理员账号添加后台用户", notes = "需要管理员权限")
    @PostMapping("/createMember")
    public WrapperResponse createMember(String username, String password, Location location, String name) {
        return adminService.createMember(username, password, location, name);
    }

    @ApiOperation(value = "管理员账号查询所有后台用户", notes = "需要管理员权限")
    @PostMapping("/listAllMembers")
    public WrapperResponse listAllMembers() {
        return adminService.listAllMembers();
    }

    @ApiOperation(value = "管理员账号修改后台用户密码", notes = "需要管理员权限")
    @PostMapping("/modifyMemberPassword")
    public WrapperResponse modifyMemberPassword(String memberId, String newPassword) {
        return adminService.modifyMemberPassword(memberId, newPassword);
    }
}
