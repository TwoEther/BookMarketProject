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
    public String create(@Valid MemberForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "members/createMemberForm";
        }
//        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
        Member member = new Member();
        member.setEmail(form.getEmail());
        member.setPassword(form.getPassword());
//        member.setAddress(address);


        memberServiceImpl.join(member);
        return "redirect:/";
    }

    @GetMapping(value = "/members")
    public String memberList(Model model){
        List<Member> allMember = memberServiceImpl.findAllMember();
        model.addAttribute("members", allMember);
        return "members/memberList";
    }
}
