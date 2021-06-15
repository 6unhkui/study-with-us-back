package switus.user.back.studywithus.repository.attendance;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import switus.user.back.studywithus.domain.attendance.Attendance;
import switus.user.back.studywithus.dto.AttendanceDto;

import static switus.user.back.studywithus.domain.attendance.QAttendance.attendance;
import static switus.user.back.studywithus.domain.room.QRoom.room;
import static switus.user.back.studywithus.domain.member.QMember.member;
import static switus.user.back.studywithus.domain.account.QAccount.account;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
public class AttendanceRepositoryImpl implements AttendanceRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Attendance findAccountAttendanceToday(Long accountId, Long roomId) {
        return queryFactory.selectFrom(attendance)
                .innerJoin(attendance.room, room).on(room.id.eq(roomId))
                .innerJoin(attendance.member, member).on(member.delFlag.eq(false))
                .innerJoin(member.account, account).on(account.id.eq(accountId))
                .where(
                        Expressions.dateOperation(
                                Date.class, Ops.DateTimeOps.DATE,
                                attendance.insDate
                        ).eq(Expressions.currentDate())
                )
                .fetchOne();
    }


    @Override
    public List<AttendanceDto.StatisticsResponse> findMemberMonthlyAttendanceCount(Long memberId) {
        StringTemplate insDateMonth = Expressions.stringTemplate("DATE_FORMAT({0},'{1s}')", attendance.insDate, ConstantImpl.create("%Y-%m"));
        return queryFactory
                .select(
                        Projections.constructor(
                                AttendanceDto.StatisticsResponse.class,
                                insDateMonth,
                                attendance.id.count()
                        )
                )
                .from(attendance)
                .leftJoin(attendance.member, member)
                .where(member.id.eq(memberId), member.delFlag.eq(false))
                .groupBy(insDateMonth)
                .orderBy(insDateMonth.asc()).fetch();
    }

}
