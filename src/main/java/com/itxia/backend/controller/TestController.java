package com.itxia.backend.controller;

import com.itxia.backend.controller.vo.WrapperResponse;
import com.itxia.backend.data.repo.OrderQueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    private final OrderQueryRepository orderQueryRepository;

    @Autowired
    public TestController(OrderQueryRepository orderQueryRepository) {
        this.orderQueryRepository = orderQueryRepository;
    }

    @GetMapping("/all")
    public WrapperResponse queryTest() {
        return WrapperResponse.wrap(orderQueryRepository.findAll(PageRequest.of(0, 10)));
    }
}
