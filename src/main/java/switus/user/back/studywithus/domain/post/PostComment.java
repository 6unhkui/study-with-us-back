package switus.user.back.studywithus.domain.post;

import lombok.Builder;
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

//    private int depth;
    private int seq;

    @Column(columnDefinition = "TEXT")
    private String content;


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private PostComment parent;

    @OneToMany(mappedBy = "parent")
    @OrderBy("seq")
    private List<PostComment> child;

    @Builder
    public PostComment(String content, int seq) {
        this.content = content;
        this.seq = seq;
    }


    public void setParent(PostComment parent) {
        this.parent = parent;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public void setWriter(Member member) {
        this.member = member;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void change(String content) {
        this.content = content;
    }


}
