package org.project.shop.domain;

import jakarta.persistence.*;
<<<<<<< HEAD
=======
import jakarta.validation.constraints.Null;
>>>>>>> 16b61c9b69a8d380269626d10c49fc8f9a30ba77
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class CategoryItem {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryItems", insertable=false, updatable=false)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryItems", insertable=false, updatable=false)
    private Category category;
<<<<<<< HEAD
=======

    public void setItem(Item item) {
        if (this.item != null) {
            this.item.getCategoryItems().remove(this);
        }
        this.item = item;
        category.getCategoryItems().add(this);
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "CategoryItem{" +
                "id=" + id +
                ", item=" + item.toString() +
                ", category=" + category.toString() +
                '}';
    }
>>>>>>> 16b61c9b69a8d380269626d10c49fc8f9a30ba77
}
