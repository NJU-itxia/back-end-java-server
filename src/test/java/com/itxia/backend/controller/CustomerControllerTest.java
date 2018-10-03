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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 测试
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

    @Test
    public void makeAppointment() throws Exception {
        mockMvc.perform(post("/customer/appointment"))
                .andExpect(status().is(400));
        mockMvc.perform(post("/customer/appointment")
                .header("id", "testCustomer"))
                .andExpect(status().is(400));
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
        mockMvc.perform(post("/customer/appointment")
                .header("id", "testCustomer")
                .characterEncoding("utf-8")
                .content(JSONObject.toJSONString(param))
                .contentType(MediaType.APPLICATION_JSON)
                .param("appointmentParam", JSONObject.toJSONString(param)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void deleteAppointment() {
    }

    @Test
    public void getCurrentAppointment() {
    }
}