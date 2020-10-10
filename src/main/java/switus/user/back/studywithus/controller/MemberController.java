package switus.user.back.studywithus.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import switus.user.back.studywithus.common.annotaion.CurrentUser;
import switus.user.back.studywithus.domain.account.Account;
import switus.user.back.studywithus.domain.member.Member;
import switus.user.back.studywithus.dto.AccountDto;
import switus.user.back.studywithus.dto.AttendanceDto;
import switus.user.back.studywithus.dto.MemberDto;
import switus.user.back.studywithus.dto.common.CommonResponse;
import switus.user.back.studywithus.dto.common.CurrentAccount;
import switus.user.back.studywithus.dto.common.PageRequest;
import switus.user.back.studywithus.service.*;

import java.util.Arrays;
import java.util.List;

@Api(tags = {"Member"})
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PostService postService;
    private final AttendanceService attendanceService;

    @ApiOperation("스터디방 멤버 가입")
    @PostMapping("/room/{roomId}/join")
    public CommonResponse join(@ApiIgnore @CurrentUser CurrentAccount account,
                               @PathVariable("roomId") Long roomId) {
        memberService.join(account.getId(), roomId);
        return CommonResponse.success();
    }


    @ApiOperation("멤버 탈퇴")
    @DeleteMapping("/room/{roomId}/member")
    public CommonResponse withdrawal(@ApiIgnore @CurrentUser CurrentAccount account,
                                     @PathVariable("roomId") Long roomId) {
        memberService.withdrawal(account.getId(), roomId);
        return CommonResponse.success();
    }


    @ApiOperation("스터디방 멤버 리스트")
    @GetMapping("/room/{roomId}/members")
    public CommonResponse roomMembers(@PathVariable("roomId") Long roomId,
                                      MemberDto.SearchRequest searchRequest,
                                      PageRequest pageRequest) {
        return CommonResponse.success(memberService.findMembers(roomId, searchRequest, pageRequest.of()));
    }


    @ApiOperation("멤버 정보")
    @GetMapping("/member/{memberId}")
    public CommonResponse member(@PathVariable("memberId") Long memberId) throws NotFoundException {
        Member member = memberService.findDetail(memberId);
        Long postCount = postService.countByMember(memberId);
        List<AttendanceDto.StatisticsResponse> attendanceStatistics = attendanceService.findMemberMonthlyAttendanceCount(memberId);
        return CommonResponse.success(new MemberDto.DetailResponse(member, postCount, attendanceStatistics));
    }


    @ApiOperation("나의 정보")
    @GetMapping("/room/{roomId}/member")
    public CommonResponse my(@PathVariable("roomId") Long roomId,
                             @ApiIgnore @CurrentUser CurrentAccount account) {
        Member member = memberService.findMembership(account.getId(), roomId);
        Long postCount = postService.countByMember(member.getId());

        MemberDto.DetailResponse response = new MemberDto.DetailResponse();
        response.setJoinDate(member.getInsDate());
        response.setRole(member.getRole());
        response.setPostCount(postCount);
        return CommonResponse.success(response);
    }


    @ApiOperation(value = "멤버 삭제", notes = "멤버 삭제 요청은 매니저만 가능합니다.")
    @DeleteMapping("/member/{memberId}")
    public CommonResponse delete(@ApiIgnore @CurrentUser CurrentAccount account,
                                 @PathVariable("memberId") Long memberId) {
        memberService.delete(memberId, account.getId());
        return CommonResponse.success();
    }


    //TODO 스터디방 매니저 권한 위임
    @ApiOperation("매니저 권한 위임")
    @PutMapping("/room/{roomId}/member/manager-delegation")
    public CommonResponse managerDelegation(@ApiIgnore @CurrentUser CurrentAccount account,
                                            @Param("memberId") Long memberId,
                                            @PathVariable("roomId") Long roomId) {
        return CommonResponse.success();
    }
}
