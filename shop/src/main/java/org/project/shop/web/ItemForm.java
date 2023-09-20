package org.project.shop.web;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter @Setter
public class ItemForm {
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;
    private String author;
    private int isbn;

    private Image file;



}
