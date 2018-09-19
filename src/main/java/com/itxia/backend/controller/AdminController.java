package com.itxia.backend.controller;

import com.itxia.backend.controller.vo.WrapperResponse;
import com.itxia.backend.data.model.Location;
import com.itxia.backend.service.AdminService;
import com.itxia.backend.service.KnightService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AdminController {

    private final KnightService knightService;

    private final AdminService adminService;

    @Autowired
    public AdminController(KnightService knightService, AdminService adminService) {
        this.knightService = knightService;
        this.adminService = adminService;
    }

    @RequestMapping("/modifyPassword")
    public WrapperResponse modifyPassword(String oldPassword, String newPassword, HttpServletRequest request) {
        String knightId = Optional.of(request).map(r -> r.getHeader("id")).orElse(null);
        return knightService.modifyPassword(knightId, oldPassword, newPassword);
    }

    @RequestMapping("/listAppointments")
    public WrapperResponse listAppointments() {
        return adminService.listAllOrder();
    }

    @RequestMapping("/acceptAppointment")
    public WrapperResponse acceptAppointment(int appointmentId, HttpServletRequest request) {
        String knightId = Optional.of(request).map(r -> r.getHeader("id")).orElse(null);
        return knightService.acceptAppointment(knightId, appointmentId);
    }

    @RequestMapping("/reply")
    public WrapperResponse reply(int appointmentId, String content, HttpServletRequest request) {
        String knightId = Optional.of(request).map(r -> r.getHeader("id")).orElse(null);
        return knightService.reply(knightId, appointmentId, content);
    }

    @RequestMapping("/createMember")
    public WrapperResponse createMember(String username, String password, Location location, String name) {
        return adminService.createMember(username, password, location, name);
    }

    @RequestMapping("/listAllMembers")
    public WrapperResponse listAllMembers() {
        return adminService.listAllMembers();
    }

    @RequestMapping("/modifyMemberPassword")
    public WrapperResponse modifyMemberPassword(String memberId, String newPassword) {
        return adminService.modifyMemberPassword(memberId, newPassword);
    }
}
