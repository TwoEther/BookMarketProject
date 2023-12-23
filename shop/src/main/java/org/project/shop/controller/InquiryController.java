package org.project.shop.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.project.shop.auth.PrincipalDetails;
import org.project.shop.config.ScriptUtils;
import org.project.shop.domain.Inquiry;
import org.project.shop.domain.Item;
import org.project.shop.domain.Member;
import org.project.shop.service.InquiryServiceImpl;
import org.project.shop.service.ItemServiceImpl;
import org.project.shop.service.MemberServiceImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/inquiry")
public class InquiryController {
    private final MemberServiceImpl memberServiceImpl;
    private final InquiryServiceImpl inquiryServiceImpl;
    private final ItemServiceImpl itemServiceImpl;

    @PostMapping(value = "/add")
    @Transactional
    public void addInquiry(HttpServletResponse response,
                           @AuthenticationPrincipal PrincipalDetails principalDetails,
                           @RequestParam String itemId,
                           @RequestParam String inquiry_text) throws IOException {
        if (principalDetails == null) {
            ScriptUtils.alertAndBackPage(response, "로그인 후 이용 가능합니다");
        } else {
            Long id = Long.parseLong(itemId);
            String username = principalDetails.getUsername();
            Member findMember = memberServiceImpl.findByUserId(username);
            Item findItem = itemServiceImpl.findOneItem(id);
            Inquiry inquiry = new Inquiry(inquiry_text);

            inquiry.setMember(findMember);
            inquiry.setItem(findItem);

            inquiryServiceImpl.save(inquiry);

            ScriptUtils.alertAndBackPage(response, "문의가 저장되었습니다");
        }
    }

    @PostMapping(value = "/answer")
    public void answerInquiry(HttpServletResponse response,
                              @RequestParam String inquiry_text,
                              @RequestParam String inquiry_id,
                              @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {
        Long id = Long.parseLong(inquiry_id);
        Inquiry findInquiry = inquiryServiceImpl.findById(id);

        if (principalDetails == null) {
            ScriptUtils.alertAndBackPage(response, "로그인 후 이용 가능합니다");
        } else {
            String username = principalDetails.getUsername();
            Member findMember = memberServiceImpl.findByUserId(username);
            Inquiry inquiry = new Inquiry(inquiry_text);
            inquiry.setMember(findMember);
            inquiry.setParent(findInquiry);
            findInquiry.setChild(inquiry);

            inquiryServiceImpl.save(inquiry);
            ScriptUtils.alertAndBackPage(response, "문의가 저장되었습니다");
        }
    }
}
