package com.itxia.backend.service;

import com.itxia.backend.controller.vo.WrapperResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminOrderServiceTest {

    @Autowired
    private AdminOrderService adminOrderService;

    @Test
    public void searchOrder() {
        WrapperResponse wrapperResponse = adminOrderService.searchOrder("仙林", "FINISHED", "Win", 0, 10);
    }
}