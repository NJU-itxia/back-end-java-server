package com.itxia.backend.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setupMock() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void logout() {
    }

    @Test
    public void modifyPassword() {
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

    @Test
    public void createMember() {
    }

    @Test
    public void listAllMembers() {
    }

    @Test
    public void modifyMemberPassword() {
    }
}