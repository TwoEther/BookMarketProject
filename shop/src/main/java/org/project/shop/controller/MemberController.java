package org.project.shop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.shop.domain.Member;
import org.project.shop.service.MemberServiceImpl;
import org.project.shop.web.MemberForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

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

        System.out.println("form.getEmail() = " + form.getEmail());
        System.out.println("validateDuplicateMember = " + memberServiceImpl.validateDuplicateMember(new Member(form.getEmail())));

        // 중복 맴버 체크
        if(!memberServiceImpl.validateDuplicateMember(new Member(form.getEmail()))){
            System.out.println("Exist in MemberRepository");
            return "redirect:/members/new";
        } else {
            System.out.println("Not Exist in MemberRepository");
            String email = form.getEmail();
            String password = form.getPassword();
            String name = form.getName();
            Member member = new Member(email, password, name);
            memberServiceImpl.join(member);
            return "redirect:/";
        }
//        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
//        member.setAddress(address);
    }


    @GetMapping(value = "/members")
    public String memberList(Model model){
        List<Member> allMember = memberServiceImpl.findAllMember();
        model.addAttribute("members", allMember);
        return "members/memberList";
    }
}
