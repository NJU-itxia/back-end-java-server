package com.itxia.backend.controller;

import com.itxia.backend.controller.vo.WrapperResponse;
import com.itxia.backend.data.model.ErrorMessage;
import com.itxia.backend.data.repo.ErrorMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yzh
 */
@RestController
@RequestMapping("/performance")
public class ErrorController {

    private final ErrorMessageRepository errorMessageRepository;

    @Autowired
    public ErrorController(ErrorMessageRepository errorMessageRepository) {
        this.errorMessageRepository = errorMessageRepository;
    }

    @PostMapping("/error")
    public WrapperResponse logError(String os, String browser, String time, String ip, String message) {
        ErrorMessage errorMessage = ErrorMessage.builder()
                .message(message)
                .browser(browser)
                .ip(ip)
                .time(time)
                .os(os).build();
        errorMessageRepository.save(errorMessage);
        return WrapperResponse.wrapSuccess();
    }
}
