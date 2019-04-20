package com.itxia.backend.controller;

import com.itxia.backend.controller.vo.WrapperResponse;
import com.itxia.backend.data.model.ErrorMessage;
import com.itxia.backend.data.repo.ErrorMessageRepository;
import com.itxia.backend.util.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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

    @PostMapping("/error/{os}/{browser}/{time}/{message}")
    public WrapperResponse logError(
            @PathVariable("os") String os,
            @PathVariable("browser") String browser,
            @PathVariable("time") String time,
            @PathVariable("message") String message,
            HttpServletRequest request) {
        ErrorMessage errorMessage = ErrorMessage.builder()
                .message(message)
                .browser(browser)
                .ip(IpUtil.getIpAddress(request))
                .time(time)
                .os(os).build();
        errorMessageRepository.save(errorMessage);
        return WrapperResponse.wrapSuccess();
    }
}
