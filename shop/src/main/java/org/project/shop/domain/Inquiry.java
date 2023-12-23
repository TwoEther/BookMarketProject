package org.project.shop.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Inquiry {
    @Id
    @GeneratedValue
    private Long id;
    private LocalDateTime created_at;

    // Null 일 경우 최상위 계층
    @ManyToOne(fetch = FetchType.LAZY)
    private Inquiry parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<Inquiry> child = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Item item;
    @Column(length = 300)
    private String content;

    public Inquiry() {
    }

    public Inquiry(String content) {
        this.content = content;
        this.created_at = LocalDateTime.now();
    }

    public void setParent(Inquiry parent) {
        this.parent = parent;
    }

    public void setChild(Inquiry child) {
        this.child.add(child);
    }

    public void setMember(Member member) {
        if (this.member != null) {
            this.member.getInquiries().remove(this);
        }
        member.getInquiries().add(this);
        this.member = member;
    }

    public void setItem(Item item) {
        if (this.item != null) {
            this.item.getInquiries().remove(this);
        }
        item.getInquiries().add(this);
        this.item = item;
    }

    @Override
    public String toString() {
        return "Inquiry{" +
                "id=" + id +
                ", created_at=" + created_at +
                ", parent=" + parent +
                ", child=" + child +
                ", member=" + member +
                ", item=" + item +
                ", content='" + content + '\'' +
                '}';
    }
}
