package switus.user.back.studywithus.repository.attendance;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import switus.user.back.studywithus.domain.attendance.Attendance;
import switus.user.back.studywithus.dto.AttendanceDto;

import java.util.List;

public interface AttendanceRepositoryCustom {
    Attendance findAccountAttendanceToday(Long accountId, Long roomId);
    List<AttendanceDto.StatisticsResponse> findMemberMonthlyAttendanceCount(Long memberId);
}
