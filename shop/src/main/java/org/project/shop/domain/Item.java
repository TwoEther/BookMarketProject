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
public abstract class Item {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany
    @JoinTable(name = "items")
    private List<Category> categories = new ArrayList<>();
}
