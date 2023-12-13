package org.project.shop.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class LikeReview {
    @Id
    @GeneratedValue
    private Long id;
    private LocalDateTime created_at;

    // 상태를 위한 변수
    private Boolean status;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "likeReviews")
    private Member member;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewLikeReview")
    private Review review;

    public LikeReview() {
        this.created_at = LocalDateTime.now();
        this.status = true;
    }

    public void setMember(Member member) {
        if (this.member != null) {
            this.member.getLikeReviews().remove(this);
        }
        member.getLikeReviews().add(this);
        this.member = member;

    }


    public void setReview(Review review) {
        this.review = review;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "LikeReview{" +
                "id=" + id +
                ", created_at=" + created_at +
                ", member=" + member +
                ", review=" + review +
                '}';
    }
}
