package org.project.shop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;
    private String category1;
    private String category2;
<<<<<<< HEAD

=======
>>>>>>> 16b61c9b69a8d380269626d10c49fc8f9a30ba77

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<CategoryItem> categoryItems = new ArrayList<>();

<<<<<<< HEAD
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;
=======
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<CategoryItem> categoryItems = new ArrayList<>();

    public Category(String category1, String category2) {
        this.category1 = category1;
        this.category2 = category2;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", category1='" + category1 + '\'' +
                ", category2='" + category2 + '\'' +
                '}';
    }
>>>>>>> 16b61c9b69a8d380269626d10c49fc8f9a30ba77
}
