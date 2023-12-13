package org.project.shop.domain;


import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
public class Review {
    @Id
    @GeneratedValue
    private Long id;

    private String created_at;
    private int score;

    @Column(length = 500)
    private String text;

    @Column(name = "like_num")
    @ColumnDefault(value = "0")
    private int likeNum;

    // ?
    private String type;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "reviews")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "reviewItems")
    private Item item;

    @OneToOne(mappedBy = "review")
    private LikeReview reviewLikeReview;

    public Review() {
        this.likeNum = 0;
    }

    public void addLike() {
        this.likeNum += 1;
    }
    public void cancelLike() {
        this.likeNum -= 1;
    }

    public static int[] countByScore(List<Review> reviews) {
        int[] countArray = new int[6];
        for (Review review : reviews) {
            int score = review.score;
            System.out.println("score = " + score);
            countArray[score] += 1;
        }
        return countArray;
    }

    public static double calculateAvgScore(List<Review> reviews) {
        int sum = 0;
        for (Review review : reviews) {
            int score = review.getScore();
            sum += score;
        }
        // 소수점 첫 째자리 까지
        return Math.round((double) sum / reviews.size() * 10) / 10.0;
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
            this.item.getReviewItems().remove(this);
        }
        item.getReviewItems().add(this);
        this.item = item;
    }

    public void setType(String type) {
        this.type = type;
    }


    public Review(int score, String text, String create_time) {
        this.likeNum = 0;
        this.score = score;
        this.text = text;
        this.created_at = create_time;
    }

    public Review(String created_at, int score, String text, String type) {
        this.created_at = created_at;
        this.score = score;
        this.text = text;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", created_at='" + created_at + '\'' +
                ", score=" + score +
                ", text='" + text + '\'' +
                ", type='" + type + '\'' +
                ", member=" + member +
                ", item=" + item +
                '}';
    }
}
