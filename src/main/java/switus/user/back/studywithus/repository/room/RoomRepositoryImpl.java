package switus.user.back.studywithus.repository.room;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import switus.user.back.studywithus.domain.room.Room;
import switus.user.back.studywithus.dto.RoomDto;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static switus.user.back.studywithus.domain.category.QCategory.category;
import static switus.user.back.studywithus.domain.file.QFileInfo.fileInfo;
import static switus.user.back.studywithus.domain.member.QRoomMember.roomMember;
import static switus.user.back.studywithus.domain.room.QRoom.room;
import static switus.user.back.studywithus.domain.account.QAccount.account;

@RequiredArgsConstructor
public class RoomRepositoryImpl implements RoomRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public Room findOneById(Long roomIdx) {
        return queryFactory.selectFrom(room)
                           .leftJoin(room.category, category).fetchJoin()
                           .leftJoin(room.roomMembers, roomMember).fetchJoin()
                           .leftJoin(room.cover, fileInfo).fetchJoin()
                           .leftJoin(roomMember.account, account).fetchJoin()
                           .where(room.id.eq(roomIdx))
                           .fetchOne();
    }

    @Override
    public Page<Room> findAllWithPagination(RoomDto.SearchRequest searchRequest, Pageable pageable) {
        QueryResults<Room> result = queryFactory.selectFrom(room)
                                                .leftJoin(room.category, category).fetchJoin()
                                                .leftJoin(room.cover, fileInfo).fetchJoin()
                                                .where(likeKeyword(searchRequest.getKeyword()))
                                                .orderBy(order(searchRequest.getOrderType()))
                                                .offset(pageable.getOffset())
                                                .limit(pageable.getPageSize())
                                                .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }


    @Override
    public Page<Room> findAllByUserIdWithPagination(Long userId, RoomDto.SearchRequest searchRequest, Pageable pageable) {
        QueryResults<Room> result = queryFactory.selectFrom(room)
                                                .innerJoin(room.roomMembers, roomMember).on(roomMember.account.id.eq(userId))
                                                .leftJoin(room.category, category).fetchJoin()
                                                .leftJoin(room.cover, fileInfo).fetchJoin()
                                                .where(
                                                        likeKeyword(searchRequest.getKeyword()),
                                                        inCategories(searchRequest.getCategories())
                                                )
                                                .orderBy(order(searchRequest.getOrderType()))
                                                .offset(pageable.getOffset())
                                                .limit(pageable.getPageSize())
                                                .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }


    @Override
    public Page<Room> findAllByCategoryIdWithPagination(Long categoryIdx, RoomDto.SearchRequest searchRequest, Pageable pageable) {
        QueryResults<Room> result = queryFactory.selectFrom(room)
                                                .innerJoin(room.category, category).on(category.id.eq(categoryIdx)).fetchJoin()
                                                .leftJoin(room.cover, fileInfo).fetchJoin()
                                                .where(likeKeyword(searchRequest.getKeyword()))
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

    private BooleanExpression inCategories(long[] categories) {
        if(categories.length == 0) {
            return null;
        }

        List<Long> list = Arrays.stream(categories).boxed().collect(Collectors.toList());
        return category.id.in(list);
    }

    private OrderSpecifier<?> order(RoomDto.SearchRequest.OrderType orderType) {
        switch (orderType) {
            case INS_DATE:
                return room.insDate.desc();
            case MEMBER_COUNT:
                return room.roomMembers.size().desc();
            default:
                return room.name.asc();
        }
    }

}
