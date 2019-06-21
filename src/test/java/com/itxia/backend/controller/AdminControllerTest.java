package com.itxia.backend.controller;

import com.itxia.backend.data.model.Location;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * 账号相关测试，最好在测试环境跑，因为会产生一堆垃圾...
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class AdminControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;


    @Before
    public void setupMock() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void modifyPassword() throws Exception {
        final String username = "test-usr-123",
                oldPassword = "test-pwd-123",
                invalidNewPassword = "shit",
                newPassword = "test-pwd-321";
        //先添加账号
        mockMvc.perform(put("/admin/member/create")
                .param("username", username)
                .param("password", oldPassword)
                .param("location", Location.GU_LOU.getValue())
                .param("name", "test-NAME")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.success").value(true))
        ;
        //更改密码:新密码不符合格式
        mockMvc.perform(post("/admin/password/modify")
                .header("id", username)
                .param("oldPassword", oldPassword)
                .param("newPassword", invalidNewPassword)

        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.success").value(false))
        ;
        //更改密码:旧密码不对
        mockMvc.perform(post("/admin/password/modify")
                .header("id", username)
                .param("oldPassword", newPassword)
                .param("newPassword", newPassword)

        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.success").value(false))
        ;
        //更改密码:正常
        mockMvc.perform(post("/admin/password/modify")
                .header("id", username)
                .param("oldPassword", oldPassword)
                .param("newPassword", newPassword)

        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.success").value(true))
        ;

    }

    @Test
    public void listAppointments() {
    }

    @Test
    public void acceptAppointment() {
    }

    @Test
    public void reply() {
    }

    /**
     * 正常添加账号
     */
    @Test
    public void createMember() throws Exception {
        mockMvc.perform(
                put("/admin/member/create")
                        .param("username", "test-usr-124")
                        .param("password", "test-pwd-123456")
                        .param("location", Location.GU_LOU.getValue())
                        .param("name", "test-name-create")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.success").value(true))
        ;
    }

    @Test
    public void listAllMembers() {
    }

    @Test
    public void modifyMemberPassword() {
    }
}