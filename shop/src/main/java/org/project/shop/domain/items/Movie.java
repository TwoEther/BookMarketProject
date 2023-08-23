package org.project.shop.domain.items;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.project.shop.domain.Item;

@Entity
@DiscriminatorColumn(name = "B")
@Getter @Setter
public class Movie extends Item {
    private String director;
    private String actor;
}
