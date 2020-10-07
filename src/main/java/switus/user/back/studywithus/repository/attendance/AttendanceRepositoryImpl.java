package switus.user.back.studywithus.repository.attendance;

import com.querydsl.core.types.Ops;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import switus.user.back.studywithus.domain.attendance.Attendance;

import static switus.user.back.studywithus.domain.attendance.QAttendance.attendance;
import static switus.user.back.studywithus.domain.room.QRoom.room;
import static switus.user.back.studywithus.domain.member.QMember.member;
import static switus.user.back.studywithus.domain.account.QAccount.account;

import java.util.Date;

@RequiredArgsConstructor
public class AttendanceRepositoryImpl implements AttendanceRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Attendance findAccountAttendanceToday(Long accountId, Long roomId) {
        return queryFactory.selectFrom(attendance)
                .innerJoin(attendance.room, room).on(room.id.eq(roomId))
                .innerJoin(attendance.member, member)
                .innerJoin(member.account, account).on(account.id.eq(accountId))
                .where(
                        Expressions.dateOperation(
                                Date.class, Ops.DateTimeOps.DATE,
                                attendance.insDate
                        ).eq(Expressions.currentDate())
                ).fetchOne();
    }
}
