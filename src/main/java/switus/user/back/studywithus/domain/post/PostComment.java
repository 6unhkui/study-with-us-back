package switus.user.back.studywithus.domain.post;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import org.springframework.data.util.Lazy;
import switus.user.back.studywithus.domain.common.BaseEntity;
import switus.user.back.studywithus.domain.account.Account;
import switus.user.back.studywithus.domain.member.Member;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity @Getter
@Where(clause = "del_Flag = false")
@NoArgsConstructor(access = PROTECTED)
public class PostComment extends BaseEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private int depth;
    private int seq;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "parent_id")
    private PostComment parent;

    @OneToMany(mappedBy = "parent")
    private List<PostComment> child;
}
