package org.project.shop.controller;

import lombok.RequiredArgsConstructor;
import org.project.shop.repository.ItemRepository;
import org.project.shop.service.CartServiceImpl;
import org.project.shop.service.ItemServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/cart")
public class CartController {
    private final CartServiceImpl cartServiceImpl;
    private final ItemServiceImpl itemServiceImpl;

    @PostMapping(value = "")
    @ResponseBody
    public boolean checkStockQuantity(@RequestParam Map<String, Object> param)  {
        Long itemId = Long.parseLong((String) param.get("itemId"));
        int quantity = Integer.parseInt((String) param.get("quantity"));

        System.out.println("quantity = " + quantity);
        // 장바구니 버튼을 클릭하면 재고를 확인
        // 재고를 확인후 장바구니 엔티티에 추가
        if (itemServiceImpl.checkStockQuantity(itemId, quantity)) {
            itemServiceImpl.orderItem(itemId, quantity);
            return true;
        }else{
            return false;
        }
    }

}
