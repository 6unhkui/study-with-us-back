package switus.user.back.studywithus.domain.room;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import switus.user.back.studywithus.domain.common.BaseEntity;
import switus.user.back.studywithus.domain.category.Category;
import switus.user.back.studywithus.domain.member.RoomMember;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Where(clause = "del_Flag = false")
@NoArgsConstructor(access = PROTECTED)
public class Room extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private String name;
    private String description;
    private int maxCount;

    @OneToMany(mappedBy = "room")
    private List<RoomMember> roomMembers = new ArrayList<>();

    @ManyToMany(mappedBy = "rooms")
    private List<Category> categories = new ArrayList<>();

    @Builder
    public Room(String name, String description, int maxCount){
        this.name = name;
        this.description = description;
        this.maxCount = maxCount;
    }
}
