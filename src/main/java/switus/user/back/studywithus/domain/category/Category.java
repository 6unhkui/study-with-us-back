package switus.user.back.studywithus.domain.category;

import lombok.Getter;
import switus.user.back.studywithus.domain.BaseEntity;
import switus.user.back.studywithus.domain.room.Room;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long idx;

    private String name;

    @ManyToMany
    @JoinTable(name = "category_item_hub",
            joinColumns = @JoinColumn(name="category_idx", referencedColumnName = "idx"),
            inverseJoinColumns = @JoinColumn(name="item_idx", referencedColumnName = "idx"))
    private List<Room> rooms = new ArrayList<>();


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_idx")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();


    public void setParent(Category parent) {
        this.parent = parent;
    }

    // == 연관관계 편의 메소드 ==
    public void addChildCategory(Category category){
        category.setParent(this);
        this.child.add(category);
    }
}
