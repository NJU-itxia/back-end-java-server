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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
     * 测试接口
     * PUT /customer/appointment
     * DELETE /customer/appointment/{appointment}
     * POST /customer/appointment/current
     * <p>
     * 场景:
     * 1. 检查用户12345678910是否有最近未完成的维修单，应为没有
     * 2. 添加一个维修单，结果为成功，并能获取单号
     * 3. 检查用户12345678910是否有最近未完成的维修单，应为有
     * 4. 再添加一个维修单，结果为 存在未完成的维修单
     * 5. 用户取消维修单，结果为成功（实际上只能取消最近的一个维修单，可能不需要传单号，考虑一下修改）
     * 6. 检查用户12345678910是否有最近未完成的维修单，应为没有
     *
     * @throws Exception 可能的异常
     */
    @Test
    public void makeAppointment() throws Exception {
        String customerId = "12345678910";
        mockMvc.perform(post("/customer/appointment/current")
                .header("id", customerId))
                .andExpect(jsonPath("$.data").isEmpty());
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
                .header("id", customerId)
                .characterEncoding("utf-8")
                .content(JSONObject.toJSONString(param))
                .contentType(MediaType.APPLICATION_JSON)
                .param("appointmentParam", JSONObject.toJSONString(param)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andReturn().getResponse().getContentAsString();
        mockMvc.perform(post("/customer/appointment/current")
                .header("id", customerId))
                .andExpect(jsonPath("$.data").exists());
        mockMvc.perform(put("/customer/appointment")
                .header("id", customerId)
                .characterEncoding("utf-8")
                .content(JSONObject.toJSONString(param))
                .contentType(MediaType.APPLICATION_JSON)
                .param("appointmentParam", JSONObject.toJSONString(param)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false));
        mockMvc.perform(delete("/customer/appointment/current")
                .header("id", customerId))
                .andExpect(jsonPath("$.success").value(true));
        mockMvc.perform(post("/customer/appointment/current")
                .header("id", customerId))
                .andExpect(jsonPath("$.data").isEmpty());
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