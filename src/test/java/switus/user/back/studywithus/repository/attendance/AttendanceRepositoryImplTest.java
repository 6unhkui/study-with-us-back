//package switus.user.back.studywithus.repository.attendance;
//
//import com.querydsl.core.QueryResults;
//import com.querydsl.core.Tuple;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import switus.user.back.studywithus.dto.AttendanceDto;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class AttendanceRepositoryImplTest {
//
//    @Autowired
//    AttendanceRepository attendanceRepository;
//
//    @Test
//    public void 멤버의_출석_현황() throws Exception {
//        //given
//        Long memberId = 2L;
//
//
//        //when
//        List<AttendanceDto.StatisticsResponse> result = attendanceRepository.findMemberMonthlyAttendanceCount(memberId);
//        for(AttendanceDto.StatisticsResponse s : result) {
//            System.out.println(s.getName());
//        }
//
//        //then
//    }
//}