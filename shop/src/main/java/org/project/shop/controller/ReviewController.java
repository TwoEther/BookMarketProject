package org.project.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/review")
public class ReviewController {
    @GetMapping(value = "/manage")
    public String reviewPage() {
        return "review/reviewPage";
    }
}
