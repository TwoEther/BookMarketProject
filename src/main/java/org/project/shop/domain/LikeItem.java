package org.project.shop.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class LikeItem {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "member")
    private Member member;

    @OneToMany(mappedBy = "likeItem")
    private List<Item> items = new ArrayList<>();

    public void setMember(Member member) {
        this.member = member;
    }

    public void addItem(Item item) {
        this.items.add(item);
    }

}
