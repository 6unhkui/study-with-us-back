package switus.user.back.studywithus.repository.postComment;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
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
import switus.user.back.studywithus.dto.RoomDto;

import java.util.List;

import static switus.user.back.studywithus.domain.account.QAccount.account;
import static switus.user.back.studywithus.domain.member.QMember.member;
import static switus.user.back.studywithus.domain.post.QPost.post;
import static switus.user.back.studywithus.domain.post.QPostComment.postComment;
import static switus.user.back.studywithus.domain.room.QRoom.room;

@RequiredArgsConstructor
public class PostCommentRepositoryImpl implements PostCommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public List<PostComment> findByPost(Long postId) {
        QueryResults<PostComment> results = queryFactory.selectFrom(postComment)
                                                        .join(postComment.post, post).on(post.id.eq(postId))
                                                        .leftJoin(postComment.member, member).fetchJoin()
                                                        .join(member.account, account).fetchJoin()
                                                        .orderBy(postComment.seq.asc())
                                                        .fetchResults();
        return results.getResults();
    }

    @Override
    public PostComment findMaxSeqByParent(Long parentId) {
        return queryFactory.selectFrom(QPostComment.postComment)
                           .where(QPostComment.postComment.parent.id.eq(parentId))
                           .orderBy(QPostComment.postComment.seq.desc())
                           .fetchFirst();
    }

    @Override
    public PostComment findMaxSeq() {
        return queryFactory.selectFrom(postComment)
                           .where(postComment.parent.isNull())
                           .orderBy(postComment.seq.desc())
                           .fetchFirst();
    }
}
