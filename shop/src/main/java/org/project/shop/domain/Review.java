package org.project.shop.domain;


import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Review {
    @Id
    @GeneratedValue
    private Long id;
    private int score;
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviews", insertable = false, updatable=false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviews", insertable = false, updatable=false)
    private Item item;

    public Review() {
    }

    public void setMember(Member member) {
        if (this.member != null) {
            this.member.getReviews().remove(this);
        }
        member.getReviews().add(this);
        this.member = member;
    }

    public void setItem(Item item) {
        if (this.item != null) {
            this.item.getReviews().remove(this);
        }
        item.getReviews().add(this);
        this.item = item;
    }

    public Review(int score, String comment) {
        this.score = score;
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", score=" + score +
                ", comment='" + comment + '\'' +
                ", member=" + member +
                ", item=" + item +
                '}';
    }
}
