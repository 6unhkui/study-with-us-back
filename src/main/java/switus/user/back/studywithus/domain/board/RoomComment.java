package switus.user.back.studywithus.domain.board;

import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import switus.user.back.studywithus.domain.common.BaseEntity;
import switus.user.back.studywithus.domain.user.User;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Where(clause = "del_Flag = false")
@NoArgsConstructor(access = PROTECTED)
public class RoomComment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long idx;

    private int depth;
    private int seq;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(referencedColumnName = "idx")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_idx")
    private RoomComment parent;

    @OneToMany(mappedBy = "parent")
    private List<RoomComment> child;
}
