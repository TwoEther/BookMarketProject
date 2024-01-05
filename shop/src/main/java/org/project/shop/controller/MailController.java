package org.project.shop.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.shop.service.MailService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/member")
public class MailController {
    private final MailService mailService;

    @ResponseBody
    @PostMapping("/email/verification-requests")
    public String sendMessage(@RequestParam("email") @Valid String email) {
        return "";
    }
}
