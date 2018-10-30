package com.itxia.backend.controller;

import com.alibaba.fastjson.JSONObject;
import com.itxia.backend.controller.vo.AppointmentParam;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 测试CustomerController提供的接口
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setupMock() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * 测试接口/customer/appointment
     * 直接post，不带header和body
     * 没有body的时候应该是400
     *
     * @throws Exception 可能的异常
     */
    @Test
    public void wrongParam() throws Exception {
        mockMvc.perform(put("/customer/appointment"))
                .andExpect(status().is(400));
        mockMvc.perform(put("/customer/appointment")
                .header("id", "testCustomer"))
                .andExpect(status().is(400));
    }

    /**
     * 测试接口/customer/appointment
     *
     * @throws Exception 可能的异常
     */
    @Test
    public void makeAppointment() throws Exception {
        AppointmentParam param = AppointmentParam.builder()
                .campus("鼓楼")
                .description("没啥描述")
                .email("32516327@qq.com")
                .name("你好啊")
                .id("none")
                .systemVersion("系统啦")
                .deviceVersion("设备啦")
                .file("no file")
                .build();
        mockMvc.perform(put("/customer/appointment")
                .header("id", "testCustomer")
                .characterEncoding("utf-8")
                .content(JSONObject.toJSONString(param))
                .contentType(MediaType.APPLICATION_JSON)
                .param("appointmentParam", JSONObject.toJSONString(param)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * 测试接口/customer/deleteAppointment
     */
    @Test
    public void deleteAppointment() {
    }

    /**
     * 测试接口/customer/getCurrentAppointment
     */
    @Test
    public void getCurrentAppointment() {

    }

    /**
     * 测试接口/customer/getAppointments
     *
     * @throws Exception 可能的异常
     */
    @Test
    public void getAppointmentsTest() throws Exception {
        mockMvc.perform(post("/customer/appointment/all")
                .header("id", "15951814859"))
                .andExpect(status().is(200))
                .andDo(print());
    }
}