package org.project.shop.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
// 추후 물품 상속관계를 위해 추상클래스로 작성
public class Item {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private int price;
    private int stockQuantity;

    private String author;
    private String publisher;
    private int isbn;
    private Date createDate;
    private int pages;
    private String description;

    @NotNull
    private String filePath;
    @NotNull
    private String fileName;

    // N:M
    @ManyToMany
    @JoinTable(name = "items")
    private List<Category> categories = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "items", unique = true)
    private Review review;

    // 재고 관리를 위한 로직
    public void addStock(int stockQuantity) {
        this.stockQuantity += stockQuantity;
    }

    // 재고 관리를 위한 로직
    public void removeStock(int stockQuantity) {
        int resStock = this.stockQuantity - stockQuantity;
        if(resStock < 0){
            throw new NotEnoughStockException("more stock");
        }
        this.stockQuantity = resStock;
    }
}
