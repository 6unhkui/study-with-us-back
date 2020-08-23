package switus.user.back.studywithus.domain.room;

import lombok.Getter;
import switus.user.back.studywithus.domain.BaseEntity;
import switus.user.back.studywithus.domain.category.Category;
import switus.user.back.studywithus.domain.member.RoomMember;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Room extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private String name;
    private String description;

    @OneToMany(mappedBy = "room")
    private List<RoomMember> roomMembers = new ArrayList<>();

    @ManyToMany(mappedBy = "rooms")
    private List<Category> categories = new ArrayList<>();
}
