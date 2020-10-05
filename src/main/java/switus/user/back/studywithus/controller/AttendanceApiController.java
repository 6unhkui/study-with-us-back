package switus.user.back.studywithus.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import switus.user.back.studywithus.common.annotaion.CurrentUser;
import switus.user.back.studywithus.domain.account.Account;
import switus.user.back.studywithus.dto.common.CommonResponse;
import switus.user.back.studywithus.dto.common.CurrentAccount;
import switus.user.back.studywithus.service.AttendanceService;

@Api(tags = {"Attendance Check"})
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AttendanceApiController {

    private final AttendanceService attendanceService;


    @ApiOperation("출석 체크")
    @PostMapping("/room/{roomId}/attendance")
    public CommonResponse attendance(@ApiIgnore @CurrentUser CurrentAccount account,
                                     @PathVariable("roomId") Long roomId) throws NotFoundException {
        return CommonResponse.success();
    }

}
