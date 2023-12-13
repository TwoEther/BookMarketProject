package org.project.shop.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
// 추후 물품 상속관계를 위해 추상클래스로 작성
public class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;
    private String name;
    private int price;
    private int stockQuantity;

    private String author;
    private String publisher;
    private int isbn;
    private LocalDateTime createDate;
    private int pages;
    private String description;

    private String filePath;
    private String fileName;


    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private List<CartItem> cartItem = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Category category;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Review> reviewItems = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "items")
    private LikeItem likeItem;

    public void setLikeItem(LikeItem likeItem) {
        this.likeItem = likeItem;
    }

    @Builder
    public Item(String name, int price, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.createDate = LocalDateTime.now();
    }

    public double calculateAvgScore() {
        int total_score =  0;
        for (Review review : this.reviewItems) {
            int score = review.getScore();
            total_score += score;
        }
        // 소수점 첫 째자리 까지
        return Math.round((double) total_score / this.reviewItems.size() * 10) / 10.0;

    }



    public Item(String name, int price, int stockQuantity, String author, String publisher, int isbn, int pages, String description) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.author = author;
        this.publisher = publisher;
        this.isbn = isbn;
        this.pages = pages;
        this.description = description;
        this.createDate = LocalDateTime.now();
    }

    public Item() {

    }
    public void setCategory(Category category) {
        if (this.category != null) {
            this.category.getItems().remove(this);
        }
        this.category = category;
        category.getItems().add(this);
    }

    // 재고 관리를 위한 로직
    public void addStock(int stockQuantity) {
        this.stockQuantity = Math.max(this.stockQuantity + stockQuantity, 0);
    }

    // 재고 관리를 위한 로직
    public void removeStock(int stockQuantity) {
        this.stockQuantity -= stockQuantity;
    }


    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                '}';
    }
}
