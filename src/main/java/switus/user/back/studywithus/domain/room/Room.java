package switus.user.back.studywithus.domain.room;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Where;
import switus.user.back.studywithus.domain.common.BaseEntity;
import switus.user.back.studywithus.domain.category.Category;
import switus.user.back.studywithus.domain.file.FileInfo;
import switus.user.back.studywithus.domain.member.RoomMember;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Where(clause = "del_Flag = false")
@Getter @NoArgsConstructor(access = PROTECTED)
public class Room extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "Text")
    private String description;

    @Column(nullable = false)
    private int maxCount;

    @OneToMany(mappedBy = "room")
    private List<RoomMember> roomMembers = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(referencedColumnName = "id")
    private Category category;

//    private Long fileIdx;
//    @OneToOne
//    @JoinColumns({@JoinColumn(referencedColumnName="idx"), @JoinColumn(referencedColumnName="group_idx")})
//    private FileInfo cover;

    @OneToOne(fetch = LAZY)
    @JoinColumns({@JoinColumn(referencedColumnName = "id")})
    private FileInfo cover;


    @Builder
    public Room(String name, String description, int maxCount){
        this.name = name;
        this.description = description;
        this.maxCount = maxCount;
    }

    public void addCategory(Category category) {
        this.category = category;
        category.getRooms().add(this);
    }

    public void setCover(FileInfo file) {
        this.cover = file;
    }


    public void addRoomMember(RoomMember roomMember) {
        roomMember.setRoom(this);
        roomMembers.add(roomMember);
    }
}
