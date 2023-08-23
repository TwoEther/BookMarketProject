package org.project.shop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter
@Setter
// 추후 물품 상속관계를 위해 추상클래스로 작성
public abstract class Item {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private int price;
    private int stockQuantity;

    // N:M
    @ManyToMany
    @JoinTable(name = "items")
    private List<Category> categories = new ArrayList<>();

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
