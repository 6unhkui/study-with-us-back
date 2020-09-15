package switus.user.back.studywithus.domain.category;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import switus.user.back.studywithus.domain.common.BaseEntity;
import switus.user.back.studywithus.domain.room.Room;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Where(clause = "del_Flag = false")
@Getter @NoArgsConstructor(access = PROTECTED)
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(length = 50)
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Room> rooms;

    @Builder
    public Category(Long id, String name) {
        this.name = name;
        this.id = id;
    }
}
