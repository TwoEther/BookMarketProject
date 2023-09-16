package org.project.shop.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.shop.domain.Member;
import org.project.shop.service.MemberServiceImpl;
import org.project.shop.web.LoginForm;
import org.project.shop.web.MemberForm;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/member")
public class MemberController {
    private final MemberServiceImpl memberServiceImpl;

    @GetMapping(value = "/new")
    public String createForm(@RequestParam(value = "error", required = false) String error,
                             @RequestParam(value = "exception", required = false) String exception,
                             Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "/member/createMemberForm";
    }



    @PostMapping(value = "/new")
    public String signUp(@Valid MemberForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "/member/createMemberForm";
        }

        String id = form.getUserId();
        String password = form.getPassword();
        Member member = new Member(id, password);
//        String name = form.getName();e
        if (memberServiceImpl.checkReqexId(id) && memberServiceImpl.checkReqexPw(password)) {
            result.reject("signupFailed", "아이디나 패스워드가 올바르지 않습니다");
            return "/member/createMemberForm";
        }

        try {
            memberServiceImpl.join(member);
        }catch(DataIntegrityViolationException e) {
            e.printStackTrace();
            result.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "/member/createMemberForm";
        }catch(Exception e) {
            e.printStackTrace();
            result.reject("signupFailed", e.getMessage());
            return "/member/createMemberForm";
        }

        return "redirect:/";

//        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
//        member.setAddress(address);
    }

    @PostMapping(value = "/idCheck")
    @ResponseBody
    public int idCheck(@RequestParam String userId) throws Exception{
        return memberServiceImpl.checkDuplicateMember(userId);
    }
    

    @GetMapping(value = "/login")
    public String createLoginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "/member/loginForm";
    }

    @PostMapping(value = "/login")
    public String login(@Valid LoginForm form, BindingResult result){
        if (result.hasErrors()) {
            return "/member/loginForm";
        }

        return "redirect:/";
    }

    @GetMapping("/member/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/";
    }

    @GetMapping(value = "/memberList")
    public String memberList(Model model){
        List<Member> allMember = memberServiceImpl.findAllMember();
        model.addAttribute("members", allMember);
        return "/member/memberList";
    }
}
