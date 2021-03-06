package switus.user.back.studywithus.domain.post;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import switus.user.back.studywithus.domain.common.BaseEntity;
import switus.user.back.studywithus.domain.file.FileGroup;
import switus.user.back.studywithus.domain.member.Member;
import switus.user.back.studywithus.domain.room.Room;
import switus.user.back.studywithus.domain.account.Account;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity @Getter
@Where(clause = "del_Flag = false")
@NoArgsConstructor(access = PROTECTED)
public class Post extends BaseEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToOne(fetch = LAZY)
    @JoinColumn(referencedColumnName = "id", columnDefinition = "BigInt")
    private FileGroup fileGroup;

    @OneToMany(mappedBy = "post")
    private List<PostComment> comments;

    @Builder
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void setFileGroup(FileGroup fileGroup) {
        this.fileGroup = fileGroup;
    }

    public void editPost(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // === 연관관계 편의 메소드 ===
    public void setRoom(Room room) {
        room.getPosts().add(this);
        this.room = room;
    }

    public void setWriter(Member member) {
        member.getPosts().add(this);
        this.member = member;
    }

}

