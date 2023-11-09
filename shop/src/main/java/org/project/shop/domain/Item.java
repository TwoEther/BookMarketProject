package org.project.shop.domain;

import jakarta.persistence.*;
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


    @ManyToOne
    @JoinColumn(name = "items", unique = true, insertable = false, updatable = false)
    private Review review;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private List<CartItem> cartItem = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item")
    private Category category;

    public Item(String name, int price, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
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
    }

    public Item() {

    }
    public void setCategory(Category category) {
        if (this.category != null) {
            this.category.getItem().remove(this);
        }
        this.category = category;
        category.getItem().add(this);
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
