package switus.user.back.studywithus.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import switus.user.back.studywithus.common.annotaion.CurrentUser;
import switus.user.back.studywithus.domain.attendance.Attendance;
import switus.user.back.studywithus.dto.AttendanceDto;
import switus.user.back.studywithus.dto.common.CommonResponse;
import switus.user.back.studywithus.dto.common.CurrentAccount;
import switus.user.back.studywithus.service.AttendanceService;

import java.util.List;
import java.util.Optional;

@Api(tags = {"Attendance Check"})
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AttendanceApiController {

    private final AttendanceService attendanceService;


    @ApiOperation("출석 체크")
    @PostMapping("/room/{roomId}/attendance")
    public CommonResponse attendance(@ApiIgnore @CurrentUser CurrentAccount account,
                                     @PathVariable("roomId") Long roomId,
                                     @RequestBody AttendanceDto.SaveRequest request) throws NotFoundException {
        Attendance save = attendanceService.save(account.getId(), roomId, request);
        return CommonResponse.success(
                AttendanceDto.MemberResponse.builder().memberId(save.getMember().getId()).time(save.getInsDate()).memo(save.getMemo()).build());
    }


    @ApiOperation("금일 멤버별 출석 리스트")
    @GetMapping("/room/{roomId}/attendance")
    public CommonResponse getAttendance(@ApiIgnore @CurrentUser CurrentAccount account,
                                        @PathVariable("roomId") Long roomId) throws NotFoundException {
        // 금일 멤버별 출석 리스트
        List<AttendanceDto.MemberResponse> members = attendanceService.findMembersAttendanceToday(roomId);
        // 현재 접속한 계정의 출석 여부
        Optional<Attendance> accountAttendanceStatus = attendanceService.findAccountAttendanceToday(account.getId(), roomId);
        return CommonResponse.success(new AttendanceDto.Response(members, accountAttendanceStatus.isPresent()));
    }


    @ApiOperation("월별 멤버 출석 횟수")
    @GetMapping("/room/{roomId}/attendance/monthly")
    public CommonResponse getMemberAttendance(@ApiIgnore @CurrentUser CurrentAccount account,
                                              @Param("date") String date,
                                              @PathVariable("roomId") Long roomId) throws NotFoundException {
        return CommonResponse.success(attendanceService.findMembersMonthlyAttendanceCount(roomId, date));
    }

}
