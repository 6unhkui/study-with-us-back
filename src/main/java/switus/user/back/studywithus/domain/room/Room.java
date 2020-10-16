package switus.user.back.studywithus.domain.room;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import switus.user.back.studywithus.domain.attendance.Attendance;
import switus.user.back.studywithus.domain.file.FileGroup;
import switus.user.back.studywithus.domain.member.Member;
import switus.user.back.studywithus.domain.post.Post;
import switus.user.back.studywithus.domain.common.BaseEntity;
import switus.user.back.studywithus.domain.category.Category;
import switus.user.back.studywithus.domain.file.FileInfo;

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

    @Column(nullable = false)
    private int joinCount;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(referencedColumnName = "id")
    private Category category;

    @OneToOne(fetch = LAZY)
    @JoinColumn(referencedColumnName = "id", columnDefinition = "BigInt comment cover image file index")
    private FileGroup cover;

    @OneToMany(mappedBy = "room")
    private List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy = "room")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "room")
    private List<Attendance> attendances = new ArrayList<>();

    @Builder
    public Room(String name, String description, int maxCount){
        this.name = name;
        this.description = description;
        this.maxCount = maxCount;
    }

    public void setCover(FileGroup fileGroup) {
        this.cover = fileGroup;
    }


    public void setCategory(Category category) {
        this.category = category;
        category.getRooms().add(this);
    }


    public void incrementJoinCount() {
        joinCount = ++joinCount;
    }

    public void decrementJoinCount() {
        joinCount = --joinCount;
    }

    public void editRoom(String name, String description, int maxCount) {
        this.name = name;
        this.description = description;
        this.maxCount = maxCount;
    }
}
