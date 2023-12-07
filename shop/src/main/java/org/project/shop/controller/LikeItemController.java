package org.project.shop.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.project.shop.auth.PrincipalDetails;
import org.project.shop.config.ScriptUtils;
import org.project.shop.domain.Item;
import org.project.shop.domain.LikeItem;
import org.project.shop.domain.Member;
import org.project.shop.service.ItemServiceImpl;
import org.project.shop.service.LikeItemServiceImpl;
import org.project.shop.service.MemberServiceImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@RequestMapping(value = "/like")
@Controller
@RequiredArgsConstructor
public class LikeItemController {
    private final ItemServiceImpl itemServiceImpl;
    private final MemberServiceImpl memberServiceImpl;
    private final LikeItemServiceImpl likeItemServiceImpl;
    @PostMapping(value = "/add")
    @Transactional
    public void addToLikeCart(HttpServletResponse response, @AuthenticationPrincipal PrincipalDetails principalDetails, @RequestParam String itemId) throws IOException {
        Long id = Long.parseLong(itemId);
        if (principalDetails == null) {
            ScriptUtils.alertAndBackPage(response, "로그인 후 가능합니다");
        }

        String username = principalDetails.getUsername();
        Member findMember = memberServiceImpl.findByUserId(username);
        Item findItem = itemServiceImpl.findOneItem(id);
        LikeItem findLikeItem = likeItemServiceImpl.findLikeItemByMemberId(username);

        if (findLikeItem == null) {
            LikeItem likeItem = new LikeItem();
            likeItem.setMember(findMember);
            likeItem.addItem(findItem);
            likeItemServiceImpl.save(likeItem);
        } else {
            findLikeItem.addItem(findItem);
        }

        ScriptUtils.alertAndBackPage(response, "찜하기에 추가 되었습니다.");
    }
}
