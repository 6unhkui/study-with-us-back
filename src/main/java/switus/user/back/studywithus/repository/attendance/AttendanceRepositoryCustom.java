package switus.user.back.studywithus.repository.attendance;

import switus.user.back.studywithus.domain.attendance.Attendance;

public interface AttendanceRepositoryCustom {
    Attendance findAccountAttendanceToday(Long accountId, Long roomId);
}
