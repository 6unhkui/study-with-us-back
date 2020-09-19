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
import switus.user.back.studywithus.domain.member.Member;
import switus.user.back.studywithus.domain.member.MemberRole;
import switus.user.back.studywithus.dto.MemberDto;

import static switus.user.back.studywithus.domain.member.QMember.member;
import static switus.user.back.studywithus.domain.room.QRoom.room;
import static switus.user.back.studywithus.domain.account.QAccount.account;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public Member findMembership(Long accountId, Long roomId) {
        return queryFactory.selectFrom(member)
                            .join(member.account, account).on(account.id.eq(accountId))
                            .join(member.room, room).on(room.id.eq(roomId))
                            .fetchOne();
    }

    @Override
    public Member findManagerByRoomId(Long roomIdx) {
        return queryFactory.selectFrom(member)
                           .join(member.account, account).fetchJoin()
                           .where(member.room.id.eq(roomIdx), member.role.eq(MemberRole.MANAGER))
                           .fetchOne();
    }


    @Override
    public Page<Member> findMembers(Long roomId, MemberDto.SearchRequest searchRequest, Pageable pageable) {
        QueryResults<Member> result = queryFactory.selectFrom(member)
                                                   .rightJoin(member.room, room).on(room.id.eq(roomId))
                                                   .leftJoin(member.account, account).fetchJoin()
                                                   .where(
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


    private OrderSpecifier<?> order(MemberDto.SearchRequest.OrderType orderType) {
        switch (orderType) {
            case JOIN_DATE:
                return member.insDate.desc();
            case ROLE:
                return member.role.desc();
            default:
                return member.account.name.asc();
        }
    }

}
