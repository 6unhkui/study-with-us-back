package switus.user.back.studywithus.repository.postComment;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import switus.user.back.studywithus.domain.post.Post;
import switus.user.back.studywithus.domain.post.PostComment;
import switus.user.back.studywithus.domain.post.QPostComment;

import static switus.user.back.studywithus.domain.account.QAccount.account;
import static switus.user.back.studywithus.domain.member.QMember.member;
import static switus.user.back.studywithus.domain.post.QPost.post;
import static switus.user.back.studywithus.domain.post.QPostComment.postComment;
import static switus.user.back.studywithus.domain.room.QRoom.room;

@RequiredArgsConstructor
public class PostCommentRepositoryImpl implements PostCommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public Page<PostComment> findByPost(Long postId, Pageable pageable) {
        QPostComment parent = new QPostComment("parent");
        QPostComment child = new QPostComment("child");

        QueryResults<PostComment> result = queryFactory.selectFrom(parent)
                                                       .rightJoin(parent.post, post).on(post.id.eq(postId))
                                                       .leftJoin(parent.child, child).fetchJoin()
                                                       .leftJoin(parent.member, member)
                                                       .leftJoin(member.account, account)
                                                       .where(parent.depth.eq(1))
                                                       .offset(pageable.getOffset())
                                                       .limit(pageable.getPageSize())
                                                       .orderBy(parent.seq.asc())
                                                       .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    private BooleanExpression equalsDepth(int depth) {
        return postComment.depth.eq(depth);
    }
}
