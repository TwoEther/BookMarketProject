package org.project.shop.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping(value = "/error")
public class ExceptionController {
    @GetMapping(value = "/403")
    public String error403() {
        return "error/403";

    }
    @GetMapping(value = "/404")
    public String error404() {
        return "error/404";
    }

    @GetMapping(value = "/500")
    public String error500() {
        return "error/500";
    }


}