package switus.user.back.studywithus.repository.attendance;

import org.springframework.data.jpa.repository.JpaRepository;
import switus.user.back.studywithus.domain.attendance.Attendance;


public interface AttendanceRepository extends JpaRepository<Attendance, Long>, AttendanceRepositoryCustom {
}
