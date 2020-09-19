package switus.user.back.studywithus.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;
import switus.user.back.studywithus.domain.account.Account;
import switus.user.back.studywithus.common.annotaion.CurrentUser;
import switus.user.back.studywithus.dto.RoomDto;
import switus.user.back.studywithus.dto.common.CommonResponse;
import switus.user.back.studywithus.dto.common.PageRequest;
import switus.user.back.studywithus.service.MemberService;
import switus.user.back.studywithus.service.RoomService;

import javax.validation.Valid;

@Api(tags = {"3. Room"})
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class RoomApiController {

    private final RoomService roomService;
    private final MemberService memberService;


    @ApiOperation("스터디방 만들기")
    @PostMapping(value = "/room", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponse<Long> create(@ApiIgnore @CurrentUser Account account,
                                       @Valid RoomDto.SaveRequest request,
                                       @RequestParam(value = "file", required = false) MultipartFile file) throws NotFoundException {

        return CommonResponse.success(roomService.create(account.getId(), request, file));
    }



    @ApiOperation("스터디방 리스트")
    @GetMapping("/rooms")
    public CommonResponse<Page<RoomDto.Response>> rooms(RoomDto.SearchRequest searchRequest,
                                                        PageRequest pageRequest) throws NotFoundException {
        return CommonResponse.success(
                roomService.findAll(searchRequest, pageRequest.of())
                           .map(room -> new RoomDto.Response(room, memberService.findManagerByRoomId(room.getId()))));
    }



    @ApiOperation("현재 사용자가 가입한 스터디방 리스트")
    @GetMapping("/user/rooms")
    public CommonResponse<Page<RoomDto.Response>> roomsByCurrentAccount(@ApiIgnore @CurrentUser Account account,
                                                                        RoomDto.SearchRequest searchRequest,
                                                                        PageRequest pageRequest) throws NotFoundException {
        return CommonResponse.success(
                roomService.findAllByAccountId(account.getId(), searchRequest, pageRequest.of())
                           .map(room -> new RoomDto.Response(room, memberService.findManagerByRoomId(room.getId()))));
    }



    @ApiOperation("스터디방 상세 보기")
    @GetMapping("/room/{roomId}")
    public CommonResponse<RoomDto.DetailResponse> room(@ApiIgnore @CurrentUser Account account,
                                                       @PathVariable("roomId") Long roomId) throws NotFoundException {
        return CommonResponse.success(new RoomDto.DetailResponse(
                        roomService.findDetail(roomId),
                        memberService.findManagerByRoomId(roomId),
                        memberService.findMembership(account.getId(), roomId)));
    }


}
