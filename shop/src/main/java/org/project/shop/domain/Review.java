package org.project.shop.domain;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Review {
    @Id
    @GeneratedValue
    private Long id;
    private int score;
    private String comment;

    @ManyToOne
    @JoinColumn(name = "review", unique = true)
    private Member member;

    @OneToMany(mappedBy = "review")
    private List<Item> items = new ArrayList<>();
}
