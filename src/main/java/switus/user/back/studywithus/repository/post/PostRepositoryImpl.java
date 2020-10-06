package switus.user.back.studywithus.repository.post;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import switus.user.back.studywithus.domain.account.QAccount;
import switus.user.back.studywithus.domain.member.QMember;
import switus.user.back.studywithus.domain.post.Post;
import switus.user.back.studywithus.dto.PostDto;

import static switus.user.back.studywithus.domain.account.QAccount.account;
import static switus.user.back.studywithus.domain.room.QRoom.room;
import static switus.user.back.studywithus.domain.post.QPost.post;

import static switus.user.back.studywithus.domain.member.QMember.member;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Post> findAllByJoinedRoomOrderByInsDate(Long accountId, Pageable pageable) {
        // 작성자
        QMember writerMember = new QMember("writerMember");
        QAccount writerAccount = new QAccount("writerAccount");

        // 현재 접속한 계정
        QMember currentMember = new QMember("currentMember");
        QAccount currentAccount = new QAccount("currentAccount");

        QueryResults<Post> result = queryFactory.select(post)
                                                .from(post)
                                                .join(post.room, room).fetchJoin()
                                                .join(room.members, currentMember)
                                                .join(currentMember.account, currentAccount).on(currentAccount.id.eq(accountId))
                                                .leftJoin(post.member, writerMember).fetchJoin()
                                                .join(writerMember.account, writerAccount).fetchJoin()
                                                .offset(pageable.getOffset())
                                                .limit(pageable.getPageSize())
                                                .orderBy(post.insDate.desc())
                                                .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }


    @Override
    public Page<Post> findAllByRoom(Long roomId, PostDto.SearchRequest searchRequest, Pageable pageable) {
        QueryResults<Post> result = queryFactory.selectFrom(post)
                                                .leftJoin(post.member, member).fetchJoin()
                                                .join(member.account, account).fetchJoin()
                                                .join(post.room, room).on(room.id.eq(roomId))
                                                .where(
                                                        likeKeyword(searchRequest.getKeyword())
                                                )
                                                .offset(pageable.getOffset())
                                                .limit(pageable.getPageSize())
                                                .orderBy(post.insDate.desc())
                                                .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public Post findDetail(Long postId) {
        return queryFactory.selectFrom(post)
                           .leftJoin(post.member, member).fetchJoin()
                           .join(member.account, account).fetchJoin()
                           .join(post.room, room).fetchJoin()
                           .where(post.id.eq(postId))
                           .fetchOne();
    }


    private BooleanExpression likeKeyword(String keyword) {
        if(StringUtils.isEmpty(keyword))
            return null;
        return post.title.like( '%' + keyword + '%');
    }

}
