package switus.user.back.studywithus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import switus.user.back.studywithus.common.error.exception.BadRequestException;
import switus.user.back.studywithus.common.util.MultilingualMessageUtils;
import switus.user.back.studywithus.domain.attendance.Attendance;
import switus.user.back.studywithus.domain.member.Member;
import switus.user.back.studywithus.dto.AttendanceDto;
import switus.user.back.studywithus.repository.attendance.AttendanceRepository;
import switus.user.back.studywithus.repository.member.MemberRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final MultilingualMessageUtils message;

    @Transactional
    public Attendance save(Long accountId, Long roomId, AttendanceDto.SaveRequest request) {
        findAccountAttendanceToday(accountId, roomId).ifPresent(value -> {
            throw new BadRequestException(message.makeMultilingualMessage("attendance.attendedToday"));
        });

        Attendance attendance = request.toEntity();
        Member member = memberService.findMembership(accountId, roomId);
        attendance.setMember(member);
        attendance.setRoom(member.getRoom());
        attendanceRepository.save(attendance);
        return attendance;
    }

    public Optional<Attendance> findAccountAttendanceToday(Long accountId, Long roomId) {
       return Optional.ofNullable(attendanceRepository.findAccountAttendanceToday(accountId, roomId));
    }

    public List<AttendanceDto.MemberResponse> findMembersAttendanceToday(Long roomId) {
        return memberRepository.findAllAttendanceToday(roomId);
    }

    public List<AttendanceDto.StatisticsResponse> findMembersAttendanceCountByDateRange(Long roomId, String startDate, String endDate) {
        return memberRepository.findAllAttendanceCountByDateRange(roomId, startDate, endDate);
    }


    public List<AttendanceDto.StatisticsResponse> findMemberMonthlyAttendanceCount(Long memberId) {
        return attendanceRepository.findMemberMonthlyAttendanceCount(memberId);
    }
}
