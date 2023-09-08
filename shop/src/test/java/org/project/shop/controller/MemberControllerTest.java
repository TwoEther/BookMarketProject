package org.project.shop.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.project.shop.service.MemberService;
import org.project.shop.service.MemberServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = MemberController.class)
@Controller
class MemberControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    MemberServiceImpl memberServiceimpl;

    @Test
    @DisplayName("Connection Test")
    public void connection_test() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk());
    }
}