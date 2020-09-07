package switus.user.back.studywithus.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import switus.user.back.studywithus.domain.room.Room;
import switus.user.back.studywithus.dto.RoomDto;

import static switus.user.back.studywithus.domain.member.QRoomMember.roomMember;
import static switus.user.back.studywithus.domain.room.QRoom.room;

public class RoomRepositoryImpl implements RoomRepositoryCustom {

    @Autowired
    private JPAQueryFactory queryFactory;

    @Override
    public Page<Room> findByUserIdx(Long userIdx, RoomDto.SearchRequest searchRequest, Pageable pageable) {
        QueryResults<Room> result = queryFactory.selectFrom(room)
                                                .innerJoin(room.roomMembers, roomMember).fetchJoin()
                                                .where(roomMember.user.idx.eq(userIdx), likeKeyword(searchRequest.getKeyword()))
                                                .orderBy(order(searchRequest.getOrderType()))
                                                .offset(pageable.getOffset())
                                                .limit(pageable.getPageSize())
                                                .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    private BooleanExpression likeKeyword(String keyword) {
        if(StringUtils.isEmpty(keyword))
            return null;
        return room.name.like( '%' + keyword + '%');
    }

    private OrderSpecifier<?> order(RoomDto.SearchRequest.OrderType orderType) {
        switch (orderType) {
//            case NAME:
//                return room.name.asc();
            case INS_DATA:
                return room.insDate.asc();
            default:
                return room.name.asc();
        }
    }

}
