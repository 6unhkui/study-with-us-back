package switus.user.back.studywithus.repository.post;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import switus.user.back.studywithus.domain.post.RoomPost;

import static switus.user.back.studywithus.domain.post.QRoomPost.roomPost;
import static switus.user.back.studywithus.domain.post.QRoomComment.roomComment;

import static switus.user.back.studywithus.domain.room.QRoom.room;
import static switus.user.back.studywithus.domain.account.QAccount.account;

@RequiredArgsConstructor
public class RoomPostRepositoryImpl implements RoomPostRepositoryCustom{

    private final JPAQueryFactory queryFactory;


    @Override
    public Page<RoomPost> findAll(Long roomId, Pageable pageable) {
        QueryResults<RoomPost> result = queryFactory.selectFrom(roomPost)
                                                    .innerJoin(roomPost.room, room).on(room.id.eq(roomId))
                                                    .innerJoin(roomPost.account, account).fetchJoin()
                                                    .leftJoin(roomPost.comments, roomComment).fetchJoin()
                                                    .offset(pageable.getOffset())
                                                    .limit(pageable.getPageSize())
                                                    .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }


}
