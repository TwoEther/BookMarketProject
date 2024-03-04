package org.project.shop.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.project.shop.auth.PrincipalDetails;
import org.project.shop.config.ScriptUtils;
import org.project.shop.domain.Inquiry;
import org.project.shop.domain.Item;
import org.project.shop.domain.Member;
import org.project.shop.domain.Review;
import org.project.shop.service.InquiryServiceImpl;
import org.project.shop.service.ItemServiceImpl;
import org.project.shop.service.MemberServiceImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

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

    @DeleteMapping(value = "/delete/{inquiryId}")
    @ResponseBody
    @Transactional
    public boolean deleteInquiry(@PathVariable Long inquiryId, HttpServletResponse response,
                                 PrincipalDetails principalDetails) {
        if (principalDetails == null) {
            return false;
        }else{
            Inquiry findInquiry = inquiryServiceImpl.findById(inquiryId);

            if (!findInquiry.getChild().isEmpty()) {
                List<Inquiry> child = findInquiry.getChild();
                child.forEach(inquiry -> inquiryServiceImpl.delete(inquiry.getId()));
            }

            inquiryServiceImpl.delete(inquiryId);
            return true;
        }
    }
}
