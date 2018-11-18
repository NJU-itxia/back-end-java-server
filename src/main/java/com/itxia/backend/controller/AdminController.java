package com.itxia.backend.controller;

import com.itxia.backend.controller.vo.WrapperResponse;
import com.itxia.backend.data.model.Location;
import com.itxia.backend.service.AdminService;
import com.itxia.backend.service.KnightService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 处理/admin的请求
 * APIOperation和ApiParam太丑了，得想办法改了
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

    @ApiOperation(value = "后台用户修改自己密码", notes = "这个是修改自己的密码的")
    @PostMapping("/password/modify")
    public WrapperResponse modifyPassword(@ApiParam(value = "当前的密码") String oldPassword,
                                          @ApiParam(value = "新密码") String newPassword,
                                          HttpServletRequest request) {

        String knightId = Optional.of(request).map(r -> r.getHeader("id")).orElse(null);
        return knightService.modifyPassword(knightId, oldPassword, newPassword);
    }

    @ApiOperation(value = "后台用户查看所有的维修单", notes = "这个接口会查询很久，谨慎使用")
    @PostMapping("/appointment/all")
    public WrapperResponse listAppointments() {
        return adminService.listAllOrder();
    }

    @ApiOperation(value = "后台用户所有维修单")
    @PostMapping("/appointment/page/{page}/size/{size}")
    public WrapperResponse listAppointmentByPage(
            @ApiParam(value = "页码") @PathVariable("page") Integer pageNum,
            @ApiParam(value = "页的大小") @PathVariable("size") Integer pageSize) {
        return adminService.listOrderBy(pageNum, pageSize);
    }

    @PostMapping("/appointment/location/{location}/state/{state}/search/{search}/page/{page}/size/{size}")
    public WrapperResponse listAppointmentByCondition(
            @ApiParam(value = "校区") @PathVariable("location") String location,
            @ApiParam(value = "状态") @PathVariable("state") String state,
            @ApiParam(value = "搜索字符串") @PathVariable("search") String search,
            @ApiParam(value = "页码") @PathVariable("page") Integer pageNum,
            @ApiParam(value = "页的大小") @PathVariable("size") Integer pageSize) {
        return adminService.listOrderBy(location, state, search, pageNum, pageSize);
    }

    @ApiOperation(value = "后台用户接单", notes = "只能对状态为新创建的预约单进行此操作")
    @PostMapping("/appointment/accept/{id}")
    public WrapperResponse acceptAppointment(@ApiParam(value = "预约单的ID") @PathVariable("id") Integer appointmentId,
                                             HttpServletRequest request) {
        String knightId = Optional.of(request).map(r -> r.getHeader("id")).orElse(null);
        return knightService.acceptAppointment(knightId, appointmentId);
    }

    @PutMapping("/appointment/reply/{id}/{content}")
    @ApiOperation(value = "后台用户对某一个预约单添加评论回复",
            notes = "用来给一个预约单添加解决问题的方式的回复、评论\n" +
                    "IT侠后台成员和此预约单的用户可以在订单的任意状态下添加评论\n" +
                    "调用此接口，请在header中设置id，表示IT侠用户的账号")
    public WrapperResponse reply(@ApiParam(value = "预约单的id") @PathVariable("id") Integer appointmentId,
                                 @ApiParam(value = "评论的具体内容") @PathVariable("content") String content,
                                 HttpServletRequest request) {
        String knightId = Optional.of(request).map(r -> r.getHeader("id")).orElse(null);
        return knightService.reply(knightId, appointmentId, content);
    }

    @ApiOperation(value = "管理员账号添加后台用户", notes = "需要管理员权限，考虑加一个参数email")
    @PutMapping("/member/create")
    public WrapperResponse createMember(@ApiParam(value = "登陆名") String username,
                                        @ApiParam(value = "密码") String password,
                                        @ApiParam(value = "校区") Location location,
                                        @ApiParam(value = "姓名") String name) {
        return adminService.createMember(username, password, location, name);
    }

    @ApiOperation(value = "管理员账号查询所有后台用户", notes = "需要管理员权限")
    @PostMapping("/member/all")
    public WrapperResponse listAllMembers(String id) {
        System.out.println(id);
        return adminService.listAllMembers();
    }

    @ApiOperation(value = "管理员账号修改后台用户密码", notes = "需要管理员权限")
    @PostMapping("/password/modify/{member}")
    public WrapperResponse modifyMemberPassword(@ApiParam(value = "IT侠账号的登录名") @PathVariable("member") String memberId,
                                                @ApiParam(value = "新的密码") String newPassword) {
        return adminService.modifyMemberPassword(memberId, newPassword);
    }
}
