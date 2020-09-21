package switus.user.back.studywithus.repository.post;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import switus.user.back.studywithus.domain.post.Post;

import static switus.user.back.studywithus.domain.post.QPost.post;
import static switus.user.back.studywithus.domain.post.QPostComment.postComment;

import static switus.user.back.studywithus.domain.member.QMember.member;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public Page<Post> findAll(Long roomId, Pageable pageable) {
        QueryResults<Post> result = queryFactory.selectFrom(post)
                                                .innerJoin(post.member, member).on(member.room.id.eq(roomId))
                                                .leftJoin(post.comments, postComment).fetchJoin()
                                                .offset(pageable.getOffset())
                                                .limit(pageable.getPageSize())
                                                .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }
}
