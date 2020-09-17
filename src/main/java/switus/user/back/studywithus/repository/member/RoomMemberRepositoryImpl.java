package switus.user.back.studywithus.repository.member;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import switus.user.back.studywithus.domain.member.RoomMember;
import switus.user.back.studywithus.domain.member.RoomMemberRole;
import switus.user.back.studywithus.domain.room.Room;
import switus.user.back.studywithus.dto.RoomDto;
import switus.user.back.studywithus.dto.RoomMemberDto;

import static switus.user.back.studywithus.domain.category.QCategory.category;
import static switus.user.back.studywithus.domain.file.QFileInfo.fileInfo;
import static switus.user.back.studywithus.domain.member.QRoomMember.roomMember;
import static switus.user.back.studywithus.domain.room.QRoom.room;
import static switus.user.back.studywithus.domain.account.QAccount.account;

@RequiredArgsConstructor
public class RoomMemberRepositoryImpl implements RoomMemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public RoomMember findMembership(Long accountId, Long roomId) {
        return queryFactory.selectFrom(roomMember)
                            .join(roomMember.account, account).on(account.id.eq(accountId))
                            .join(roomMember.room, room).on(room.id.eq(roomId))
                            .fetchOne();
    }

    @Override
    public RoomMember findManagerByRoomId(Long roomIdx) {
        return queryFactory.selectFrom(roomMember)
                           .join(roomMember.account, account).fetchJoin()
                           .where(roomMember.room.id.eq(roomIdx), roomMember.role.eq(RoomMemberRole.MANAGER))
                           .fetchOne();
    }


    @Override
    public Page<RoomMember> findMembers(Long roomId, RoomMemberDto.SearchRequest searchRequest, Pageable pageable) {
        QueryResults<RoomMember> result = queryFactory.selectFrom(roomMember)
                                                      .innerJoin(roomMember.account, account).fetchJoin()
                                                      .where(
                                                              roomMember.room.id.eq(roomId),
                                                              likeKeyword(searchRequest.getKeyword())
                                                      )
                                                      .orderBy(order(searchRequest.getOrderType()))
                                                      .offset(pageable.getOffset())
                                                      .limit(pageable.getPageSize())
                                                      .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }


    private BooleanExpression likeKeyword(String keyword) {
        if(StringUtils.isEmpty(keyword))
            return null;
        return account.name.like( '%' + keyword + '%');
    }

    private OrderSpecifier<?> order(RoomMemberDto.SearchRequest.OrderType orderType) {
        switch (orderType) {
            case JOIN_DATE:
                return roomMember.insDate.desc();
            case ROLE:
                return roomMember.role.desc();
            default:
                return roomMember.account.name.asc();
        }
    }

}
