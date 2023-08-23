package org.project.shop.domain.items;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.project.shop.domain.Item;

@Entity
@DiscriminatorColumn(name = "A")
@Getter @Setter
public class Album extends Item {
    private String artist;
    private String etc;
}
