package switus.user.back.studywithus.repository.member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import switus.user.back.studywithus.domain.member.Member;
import switus.user.back.studywithus.dto.AttendanceDto;
import switus.user.back.studywithus.dto.MemberDto;

import java.util.List;

public interface MemberRepositoryCustom {
    Member findMembership(Long accountId, Long roomId);
    Member findMembership(Long memberId);
    Member findManagerByRoomId(Long roomId);
    Page<MemberDto.Response> findMembers(Long roomId, MemberDto.SearchRequest searchRequest, Pageable pageable);
    Member findDetail(Long memberId);

    List<AttendanceDto.MemberResponse> findAllAttendanceToday(Long roomId);
    List<AttendanceDto.StatisticsResponse> findAllAttendanceCountByDateRange(Long roomId, String startDate, String endDate);

}
