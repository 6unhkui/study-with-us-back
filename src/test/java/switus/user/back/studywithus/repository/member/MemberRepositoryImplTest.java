//package switus.user.back.studywithus.repository.member;
//
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//import switus.user.back.studywithus.domain.attendance.Attendance;
//import switus.user.back.studywithus.domain.member.Member;
//import switus.user.back.studywithus.domain.room.Room;
//import switus.user.back.studywithus.dto.AttendanceDto;
//import switus.user.back.studywithus.repository.attendance.AttendanceRepository;
//import switus.user.back.studywithus.repository.room.RoomRepository;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class MemberRepositoryImplTest {
//
//    @Autowired
//    private AttendanceRepository attendanceRepository;
//
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Autowired
//    private RoomRepository roomRepository;
//
//    @Test
//    public void 특정_기간_동안의_멤버별_출석_기록() throws Exception {
//        //given
//        Long roomId = 1L;
//        Room room = roomRepository.findById(roomId).get();
//
//        Long memberId = 1L;
//        Member member = memberRepository.findById(memberId).get();
//
//        Attendance attendance = Attendance.builder().memo("출석체크").build();
//        attendance.setRoom(room);
//        attendance.setMember(member);
//        attendanceRepository.save(attendance);
//
//        //when
//        List<AttendanceDto.StatisticsResponse> result =
//                memberRepository.findAllAttendanceCountByDateRange(
//                        roomId,
//                        LocalDate.of(2020,10,1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
//                        LocalDate.of(2020,10,30).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//
//        //then
//    }
//
//    @Test
//    public void 금일_출석_멤버() throws Exception {
//        //given
//        Long roomId = 1L;
//
//        //when
//        List<AttendanceDto.MemberResponse> member = memberRepository.findAllAttendanceToday(1L);
//
//        for(AttendanceDto.MemberResponse r : member) {
//            System.out.println(r.getName());
//        }
//        //then
//    }
//
//
//}