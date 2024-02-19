package org.project.shop.web;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.time.LocalDateTime;

@Getter @Setter
public class ItemForm {
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
    private String category1;
    private String category2;


    public ItemForm(){
        this.createDate = LocalDateTime.now();
    }

    private Image file;



}
