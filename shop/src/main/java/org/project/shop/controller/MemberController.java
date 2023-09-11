package org.project.shop.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.shop.domain.Member;
import org.project.shop.service.MemberServiceImpl;
import org.project.shop.web.LoginForm;
import org.project.shop.web.MemberForm;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberServiceImpl memberServiceImpl;

    @GetMapping(value = "/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping(value = "/members/new")
    public String signUp(@Valid MemberForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        String id = form.getUserId();
        String password = form.getPassword();
        Member member = new Member(id, password);
//        String name = form.getName();e
        try {
            memberServiceImpl.join(member);
        }catch(DataIntegrityViolationException e) {
            e.printStackTrace();
            result.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "members/createMemberForm";
        }catch(Exception e) {
            e.printStackTrace();
            result.reject("signupFailed", e.getMessage());
            return "members/createMemberForm";
        }

        return "redirect:/";

//        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
//        member.setAddress(address);
    }

    @PostMapping(value = "member/idCheck")
    @ResponseBody
    public int idCheck(@RequestParam String userId) throws Exception{
        return memberServiceImpl.checkDuplicateMember(userId);
    }
    

    @GetMapping(value = "members/login")
    public String createLoginForm(Model model) {
        model.addAttribute("loginForm", new MemberForm());
        return "members/loginForm";
    }

    @PostMapping(value = "members/login")
    public String login(@Valid LoginForm form, BindingResult result){
        if (result.hasErrors()) {
            return "members/createLoginForm";
        }
        return "redirect:/";
    }

    @GetMapping(value = "/members")
    public String memberList(Model model){
        List<Member> allMember = memberServiceImpl.findAllMember();
        model.addAttribute("members", allMember);
        return "members/memberList";
    }
}
