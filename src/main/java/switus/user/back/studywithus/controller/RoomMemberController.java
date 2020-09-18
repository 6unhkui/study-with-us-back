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
import switus.user.back.studywithus.domain.account.Account;
import switus.user.back.studywithus.dto.RoomMemberDto;
import switus.user.back.studywithus.dto.common.CommonResponse;
import switus.user.back.studywithus.dto.common.PageRequest;
import switus.user.back.studywithus.service.RoomMemberService;
import switus.user.back.studywithus.service.RoomService;

@Api(tags = {"4. Room Member"})
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class RoomMemberController {

    private final RoomService roomService;
    private final RoomMemberService roomMemberService;


    @ApiOperation("스터디방 가입")
    @PostMapping("/room/{roomId}/join")
    public CommonResponse join(@ApiIgnore @CurrentUser Account account,
                               @PathVariable("roomId") Long roomId) throws NotFoundException {
        roomMemberService.join(account.getId(), roomId);
        return CommonResponse.success();
    }


    @ApiOperation("멤버 탈퇴")
    @DeleteMapping("/room/{roomId}")
    public CommonResponse deleteMember(@ApiIgnore @CurrentUser Account account,
                                       @PathVariable("roomId") Long roomId) throws NotFoundException {
        roomMemberService.deleteMember(account.getId(), roomId);
        return CommonResponse.success();
    }


    @ApiOperation("스터디방 멤버 조회")
    @GetMapping("/room/{roomId}/members")
    public CommonResponse members(@PathVariable("roomId") Long roomId,
                                  RoomMemberDto.SearchRequest searchRequest,
                                  PageRequest pageRequest) throws NotFoundException {
        return CommonResponse.success(roomMemberService.findMembers(roomId, searchRequest, pageRequest.of()).map(RoomMemberDto.Response::new));
    }


    //TODO 스터디방 매니저 권한 위임
    @ApiOperation("매니저 권한 위임")
    @PutMapping("/room/{roomId}/member/manager-delegation")
    public CommonResponse managerDelegation(@ApiIgnore @CurrentUser Account account,
                                            @Param("memberId") Long memberId,
                                            @PathVariable("roomId") Long roomId) throws NotFoundException {
        return CommonResponse.success();
    }

}