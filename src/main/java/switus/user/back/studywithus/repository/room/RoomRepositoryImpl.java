package switus.user.back.studywithus.repository.room;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import switus.user.back.studywithus.domain.member.Member;
import switus.user.back.studywithus.domain.room.Room;
import switus.user.back.studywithus.dto.MemberDto;
import switus.user.back.studywithus.dto.RoomDto;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static switus.user.back.studywithus.domain.account.QAccount.account;
import static switus.user.back.studywithus.domain.category.QCategory.category;
import static switus.user.back.studywithus.domain.file.QFileGroup.fileGroup;
import static switus.user.back.studywithus.domain.file.QFileInfo.fileInfo;
import static switus.user.back.studywithus.domain.member.QMember.member;
import static switus.user.back.studywithus.domain.room.QRoom.room;

@RequiredArgsConstructor
public class RoomRepositoryImpl implements RoomRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public Room findDetail(Long roomId) {
        return queryFactory.selectFrom(room)
                .leftJoin(room.category, category).fetchJoin()
                .where(room.id.eq(roomId))
                .fetchOne();
    }


    @Override
    public Page<Room> findAll(RoomDto.SearchRequest searchRequest, Pageable pageable) {
        QueryResults<Room> result = queryFactory.selectFrom(room)
                                                .leftJoin(room.category, category).fetchJoin()
                                                .leftJoin(room.cover, fileGroup).fetchJoin()
                                                .leftJoin(fileGroup.files, fileInfo).fetchJoin()
                                                .where(
                                                        likeKeyword(searchRequest.getKeyword()),
                                                        inCategories(searchRequest.getCategoryIds())
                                                )
                                                .orderBy(order(searchRequest.getSortBy()))
                                                .offset(pageable.getOffset())
                                                .limit(pageable.getPageSize())
                                                .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }


    @Override
    public Page<Room> findAllByAccount(Long accountId, RoomDto.SearchRequest searchRequest, Pageable pageable) {
        QueryResults<Room> result = queryFactory.selectFrom(room)
                                                .join(room.members, member).on(member.account.id.eq(accountId), member.delFlag.eq(false))
                                                .leftJoin(room.category, category).fetchJoin()
                                                .leftJoin(room.cover, fileGroup).fetchJoin()
                                                .leftJoin(fileGroup.files, fileInfo).fetchJoin()
                                                .where(
                                                        likeKeyword(searchRequest.getKeyword()),
                                                        inCategories(searchRequest.getCategoryIds())
                                                )
                                                .orderBy(order(searchRequest.getSortBy()))
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


    private BooleanExpression inCategories(long[] categoriesId) {
        if(null == categoriesId || categoriesId.length == 0) {
            return null;
        }

        List<Long> list = Arrays.stream(categoriesId).boxed().collect(Collectors.toList());
        return category.id.in(list);
    }


    private OrderSpecifier<?> order(RoomDto.SearchRequest.SortBy sortBy) {
        switch (sortBy) {
            case CREATED_DATE:
                return room.insDate.desc();
            case JOIN_COUNT:
                return room.joinCount.desc();
            default:
                return room.name.asc();
        }
    }

}
