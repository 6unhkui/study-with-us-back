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
import switus.user.back.studywithus.dto.AccountDto;
import switus.user.back.studywithus.dto.MemberDto;
import switus.user.back.studywithus.dto.common.CommonResponse;
import switus.user.back.studywithus.dto.common.CurrentAccount;
import switus.user.back.studywithus.dto.common.PageRequest;
import switus.user.back.studywithus.service.AccountService;
import switus.user.back.studywithus.service.MemberService;
import switus.user.back.studywithus.service.RoomService;

import java.util.Arrays;
import java.util.List;

@Api(tags = {"4. Member"})
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @ApiOperation("스터디방 멤버 가입")
    @PostMapping("/room/{roomId}/join")
    public CommonResponse join(@ApiIgnore @CurrentUser CurrentAccount account,
                               @PathVariable("roomId") Long roomId) throws NotFoundException {
        memberService.join(account.getId(), roomId);
        return CommonResponse.success();
    }


    @ApiOperation("멤버 탈퇴")
    @DeleteMapping("/room/{roomId}/member")
    public CommonResponse withdrawal(@ApiIgnore @CurrentUser CurrentAccount account,
                                       @PathVariable("roomId") Long roomId) throws NotFoundException {
        memberService.withdrawal(account.getId(), roomId);
        return CommonResponse.success();
    }


    @ApiOperation("스터디방 멤버 리스트")
    @GetMapping("/room/{roomId}/members")
    public CommonResponse roomMembers(@PathVariable("roomId") Long roomId,
                                      MemberDto.SearchRequest searchRequest,
                                      PageRequest pageRequest) throws NotFoundException {
        return CommonResponse.success(memberService.findMembers(roomId, searchRequest, pageRequest.of()).map(MemberDto.Response::new));
    }


    //TODO 스터디방 매니저 권한 위임
    @ApiOperation("매니저 권한 위임")
    @PutMapping("/room/{roomId}/member/manager-delegation")
    public CommonResponse managerDelegation(@ApiIgnore @CurrentUser CurrentAccount account,
                                            @Param("memberId") Long memberId,
                                            @PathVariable("roomId") Long roomId) throws NotFoundException {
        return CommonResponse.success();
    }

//    @ApiOperation("멤버 정보")
//    @GetMapping("/room/{roomId}/member/{accountId}")
//    public CommonResponse member(@PathVariable("roomId") Long roomId,
//                                 @PathVariable("accountId") Long accountId) throws NotFoundException {
//        return CommonResponse.success(memberService.findById(memberId));
//    }
}
