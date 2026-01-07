package com.bootdo.modular.system.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author L
 */
@Tag(name = "异常")
@Controller
public class MainsiteErrorController implements ErrorController {
    private static final String ERROR_PATH = "/error";

    @RequestMapping(value = ERROR_PATH)
    public String handleError() {
        return "system/error/404";
    }

}