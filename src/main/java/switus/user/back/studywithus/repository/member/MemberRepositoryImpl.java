package switus.user.back.studywithus.repository.member;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import switus.user.back.studywithus.domain.member.Member;
import switus.user.back.studywithus.domain.member.MemberRole;
import switus.user.back.studywithus.dto.AttendanceDto;
import switus.user.back.studywithus.dto.MemberDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static switus.user.back.studywithus.domain.member.QMember.member;
import static switus.user.back.studywithus.domain.room.QRoom.room;
import static switus.user.back.studywithus.domain.account.QAccount.account;
import static switus.user.back.studywithus.domain.post.QPost.post;
import static switus.user.back.studywithus.domain.attendance.QAttendance.attendance;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Member findMembership(Long accountId, Long roomId) {
        return queryFactory.selectFrom(member)
                           .join(member.account, account).on(account.id.eq(accountId))
                           .join(member.room, room).on(room.id.eq(roomId))
                           .where(member.delFlag.eq(false))
                           .fetchOne();
    }

    @Override
    public Member findMembership(Long memberId) {
        return queryFactory.selectFrom(member)
                .join(member.account, account).fetchJoin()
                .join(member.room, room).fetchJoin()
                .where(
                        member.id.eq(memberId),
                        member.delFlag.eq(false)
                )
                .fetchOne();
    }

    @Override
    public Member findManagerByRoomId(Long roomIdx) {
        return queryFactory.selectFrom(member)
                           .join(member.account, account).fetchJoin()
                           .where(
                                   member.delFlag.eq(false),
                                   member.room.id.eq(roomIdx),
                                   member.role.eq(MemberRole.MANAGER)
                           )
                           .fetchOne();
    }


    @Override
    public Page<MemberDto.Response> findMembers(Long roomId, MemberDto.SearchRequest searchRequest, Pageable pageable) {
        QueryResults<MemberDto.Response> result = queryFactory.select(
                                                        Projections.constructor(MemberDto.Response.class,
                                                            member.id,
                                                            member.role,
                                                            account.name,
                                                            account.email,
                                                            account.profileImg
                                                        ))
                                                    .from(member)
                                                    .rightJoin(member.room, room).on(room.id.eq(roomId))
                                                    .join(member.account, account).on(account.delFlag.eq(false))
                                                    .where(
                                                        member.delFlag.eq(false),
                                                        likeKeyword(searchRequest.getKeyword())
                                                    )
                                                    .orderBy(
                                                        order(MemberDto.SearchRequest.OrderType.ROLE),
                                                        order(searchRequest.getOrderType())
                                                    )
                                                    .offset(pageable.getOffset())
                                                    .limit(pageable.getPageSize())
                                                    .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public Member findDetail(Long memberId) {
        return queryFactory.selectFrom(member)
                           .join(member.account, account).fetchJoin()
                           .leftJoin(member.posts, post).fetchJoin()
                           .where(
                                member.delFlag.eq(false),
                                member.id.eq(memberId)
                           )
                           .fetchOne();
    }

    @Override
    public List<AttendanceDto.MemberResponse> findAllAttendanceToday(Long roomId) {
        return  queryFactory.select(
                    Projections.constructor(AttendanceDto.MemberResponse.class,
                            member.id,
                            account.name,
                            account.profileImg,
                            attendance.memo,
                            attendance.insDate
                    ))
                .from(member)
                .innerJoin(member.room, room).on(room.id.eq(roomId))
                .join(member.account, account).on(account.delFlag.eq(false))
                .leftJoin(member.attendances, attendance).on(
                        Expressions.dateOperation(
                                Date.class, Ops.DateTimeOps.DATE,
                                attendance.insDate
                        ).eq(Expressions.currentDate())
                )
                .where(
                        member.delFlag.eq(false)
                ).fetchResults().getResults();
    }


    @Override
    public List<AttendanceDto.StatisticsResponse> findAllAttendanceCountByDateRange(Long roomId, String startDate, String endDate) {
        return queryFactory.select(
                Projections.constructor(AttendanceDto.StatisticsResponse.class,
                        account.name,
                        attendance.count()
                ))
                .from(member)
                .innerJoin(member.room, room).on(room.id.eq(roomId))
                .join(member.account, account).on(account.delFlag.eq(false))
                .leftJoin(member.attendances, attendance).on(
                        attendance.insDate.between(
                                LocalDate.parse(startDate).atStartOfDay(),
                                LocalDate.parse(endDate).atTime(23,59)
                        )
                )
                .where(
                        member.delFlag.eq(false)
                )
                .groupBy(member).fetchResults().getResults();
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
            case NAME:
                return member.account.name.asc();
            default:
                return member.role.desc();
        }
    }
}
