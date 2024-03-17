package org.project.shop.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.shop.auth.PrincipalDetails;
import org.project.shop.config.ScriptUtils;
import org.project.shop.domain.*;
import org.project.shop.service.*;
import org.project.shop.web.AddressForm;
import org.project.shop.web.LoginForm;
import org.project.shop.web.MemberForm;
import org.project.shop.web.findIDForm;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/member")
public class MemberController {
    private final MemberServiceImpl memberServiceImpl;
    private final OrderServiceImpl orderServiceImpl;
    private final OrderItemServiceImpl orderItemServiceImpl;
    private final ReviewServiceImpl reviewServiceImpl;
    private final InquiryServiceImpl inquiryServiceImpl;
    private final MailService mailService;
    private final RedisService redisService;
    private final PasswordEncoder passwordEncoder;
    
    @GetMapping(value = "/new")
    public String createForm(@RequestParam(value = "error", required = false) String error,
                             @RequestParam(value = "exception", required = false) String exception,
                             Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "member/createMemberForm";
    }

    @ModelAttribute("roles")
    private Role[] roles() {
        return Role.values();
    }

    @PostMapping(value = "/new")
    @Transactional
    public String signUp(HttpServletResponse response, @Valid MemberForm form, BindingResult result) throws Exception {
        if (result.hasErrors()) {
            return "member/createMemberForm";
        }

        String id = form.getUserId();
        String password = form.getPassword1();
        String name = form.getName();
        String phoneNum = form.getPhoneNum();
        String email = form.getEmail();
//        String roles = form.getRoles();
        String email_check_num = form.getEmail_Check_number();

        mailService.sendSimpleMessage(email);

        if (memberServiceImpl.findByUserId(id) != null) {
            ScriptUtils.alert(response, "아이디가 존재합니다");
            return "member/createMemberForm";
        } else if (email_check_num.isEmpty()) {
            ScriptUtils.alert(response, "이메일 인증후 회원가입 가능합니다");
            return "member/createMemberForm";
        } else if (!redisService.getRedisTemplateValue(email).equals(email_check_num)) {
            ScriptUtils.alert(response, "인증번호가 일치 하지 않습니다");
            return "member/createMemberForm";
        } else {
            Member member = new Member(id, password, name, phoneNum, email);

//            if (roles.equals(Role.ROLE_ADMIN.toString())) {
//                member.setRole(Role.ROLE_ADMIN.toString());
//            } else if (roles.equals(Role.ROLE_ANONYMOUS)) {
//                member.setRole(Role.ROLE_ANONYMOUS.toString());
//            } else {
//                member.setRole(Role.ROLE_USER.toString());
//            }

            if (id.equals("superadmin123")) {
                member.setRole(Role.ROLE_ADMIN.toString());
            }else{
                member.setRole(Role.ROLE_USER.toString());
            }

            if (memberServiceImpl.checkReqexId(id) && memberServiceImpl.checkReqexPw(password)) {
                result.reject("signupFailed", "아이디나 패스워드가 올바르지 않습니다");
                return "member/createMemberForm";
            }

            try {
                memberServiceImpl.save(member);
            } catch (DataIntegrityViolationException e) {
                e.printStackTrace();
                result.reject("signupFailed", "이미 등록된 사용자입니다.");
                return "member/createMemberForm";
            } catch (Exception e) {
                e.printStackTrace();
                result.reject("signupFailed", e.getMessage());
                return "member/createMemberForm";
            }
            ScriptUtils.alert(response, "회원가입이 완료 되었습니다.");
            return "home";
        }


    }

    @PostMapping(value = "/idCheck")
    @ResponseBody
    public String idCheck(@RequestParam String userId) throws Exception {
        return memberServiceImpl.checkDuplicateMember(userId);
    }

    @PostMapping(value = "/pwCheck")
    @ResponseBody
    public boolean pwCheck(@RequestParam String pw) throws Exception {
        return memberServiceImpl.checkReqexPw(pw);
    }

    @GetMapping(value = "/login")
    public String createLoginForm(@RequestParam(value = "error", required = false) String error,
                                  @RequestParam(value = "exception", required = false) String exception,
                                  Model model) {
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        model.addAttribute("loginForm", new LoginForm());
        return "member/loginForm";
    }

    @PostMapping(value = "/login")
    public String login(@Valid LoginForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "member/loginForm";
        }

        return "redirect:/";
    }

    @GetMapping("/findId")
    public String findId(Model model) {
        model.addAttribute("findIDForm", new findIDForm());
        return "member/findUserId";
    }
    @PostMapping("/findId")
    public String findIdPost(HttpServletResponse response, @RequestParam String phoneNum, @RequestParam String email, Model model) throws IOException {
        String findMemberID = memberServiceImpl.findMemberIdByEmailAndPhoneNum(email, phoneNum);

        if (findMemberID != null) {
            model.addAttribute("userId", findMemberID);
            return "member/resultId";
        }else{
            ScriptUtils.alert(response, "핸드폰 번호 또는 이메일이 다릅니다");
            return "member/findUserId";
        }

    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/";
    }

    @GetMapping(value = "/memberList")
    public String memberList(Model model) {
        List<Member> allMember = memberServiceImpl.findAllMember();
        model.addAttribute("members", allMember);
        return "member/memberList";
    }

    @GetMapping(value = "/address")
    public String setDeliveryMember(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                    Model model) throws IOException {
        String username = principalDetails.getUsername();
        Member findMember = memberServiceImpl.findByUserId(username);
        Address address = findMember.getAddress();

        String zipcode = (address == null) ? "우편 번호" : address.getZipcode();
        String address1 = (address == null) ? "주소" : address.getAddress1();
        String address2 =  (address == null) ? "상세 주소" :address.getAddress2();
        String reference = (address == null) ? "참고 항목" :address.getReference();


        model.addAttribute("zipcode", zipcode);
        model.addAttribute("address1", address1);
        model.addAttribute("address2", address2);
        model.addAttribute("reference", reference);

        model.addAttribute("AddressForm", new AddressForm());

        return "member/address";
    }

    @PostMapping(value = "/address")
    @Transactional
    public void setDeliveryMemberPost(@AuthenticationPrincipal PrincipalDetails principalDetails, HttpServletResponse response, AddressForm addressForm) throws IOException {
        String zipcode = addressForm.getZipcode();
        String address1 = addressForm.getAddress1();
        String address2 = addressForm.getAddress2();
        String reference = addressForm.getReference();

        String username = principalDetails.getUsername();
        Member findMember = memberServiceImpl.findByUserId(username);
        Address address = new Address(zipcode, address1, address2, reference);
        findMember.setAddress(address);
        ScriptUtils.alertAndBackPage(response,"배송지가 설정 되었습니다");
    }

    @GetMapping(value = "/review")
    public String getReviewPage(@AuthenticationPrincipal PrincipalDetails principalDetails
                                ,Model model) {
        if (principalDetails == null) {
            return "home";
        }
        String username = principalDetails.getUsername();
        Member findMember = memberServiceImpl.findByUserId(username);

        List<Review> allReviewByMemberId = reviewServiceImpl.findAllReviewByMemberId(findMember.getId());

        model.addAttribute("reviews", allReviewByMemberId);
        return "member/myPageReview";
    }

    @GetMapping(value = "/inquiry")
    public String getInquiryPage(@AuthenticationPrincipal PrincipalDetails principalDetails
            ,Model model) {
        if (principalDetails == null) {
            return "home";
        }
        String username = principalDetails.getUsername();
        Member findMember = memberServiceImpl.findByUserId(username);

        List<Inquiry> allInquiryByMemberId = inquiryServiceImpl.findAllInquiryByMemberId(findMember.getId());

        
        model.addAttribute("inquires", allInquiryByMemberId);
        return "member/myPageInquiry";
    }

    @GetMapping(value = "/cancel")
    public String getMemberCancelPage(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails == null) {
            return "redirect:/";
        }
        return "member/cancel";
    }

    @DeleteMapping(value = "/cancel")
    @ResponseBody
    @Transactional
    public String deleteMemberGeneral(HttpServletRequest request,
                                    HttpServletResponse response,
                                    @AuthenticationPrincipal PrincipalDetails principalDetails,
                                    @RequestParam(value = "password1", required = false, defaultValue = "") String password1,
                                    @RequestParam(value = "password2", required = false, defaultValue = "")  String password2) throws IOException {
        String username = principalDetails.getUsername();
        String msg = "";
        Member findMember = memberServiceImpl.findByUserId(username);

        if (password1.isEmpty() || password2.isEmpty()) {
            msg = "비밀번호를 입력해주세요";
        }

        else if (!password1.equals(password2)) {
            msg = "비밀 번호가 서로 다릅니다";
        }

        else if (passwordEncoder.matches(findMember.getPassword(), password1)) {
            msg = "입력하신 비밀 번호가 다릅니다";
        } else {
            msg = "삭제 되었습니다";
            new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
            orderServiceImpl.findByMemberIdAfterPayment(findMember.getId()).forEach(order -> orderItemServiceImpl.deleteOrderItem(order.getId()));
            orderServiceImpl.deleteByMemberId(findMember.getId());
            memberServiceImpl.deleteMemberByMemberId(findMember.getId());
        }
        return msg;
    }
    // admin
    @DeleteMapping(value = "/delete/{memberNum}")
    @Transactional
    @ResponseBody
    public void deleteMemberAdmin(@RequestParam String memberNum,
                                  @AuthenticationPrincipal PrincipalDetails principalDetails) {
        memberServiceImpl.deleteMemberByMemberId(Long.valueOf(memberNum));
    }

    // 이메일 인증
    @PostMapping("/new/email-check")
    @ResponseBody
    public void sendCodeToEmail(@RequestParam String email) throws Exception {
        if (!email.isEmpty()) {
            String code = mailService.sendSimpleMessage(email);
            redisService.setRedisTemplate(email, code, Duration.ofMillis(300 * 1000));
            String value = redisService.getRedisTemplateValue(email);
        }
    }

    private void checkDuplicatedEmail(String email) {
    }
}
