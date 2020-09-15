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
import switus.user.back.studywithus.dto.common.CurrentUser;
import switus.user.back.studywithus.dto.RoomDto;
import switus.user.back.studywithus.dto.common.CommonResponse;
import switus.user.back.studywithus.dto.common.PageRequest;
import switus.user.back.studywithus.service.RoomMemberService;
import switus.user.back.studywithus.service.RoomService;

import javax.validation.Valid;

@Api(tags = {"3. Room"})
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class RoomApiController {

    private final RoomService roomService;
    private final RoomMemberService roomMemberService;


    @ApiOperation(value = "스터디방 만들기")
//    @RequestMapping(value = "/room", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PostMapping(value = "/room", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponse<Long> create(@ApiIgnore @CurrentUser Account account,
                                       @Valid RoomDto.SaveRequest request,
                                       @RequestParam(value = "file", required = false) MultipartFile file) throws NotFoundException {
        return CommonResponse.success(roomService.create(account.getId(), request, file));
    }


    @ApiOperation(value = "스터디방 가입")
    @PostMapping("/room/{idx}/join")
    public CommonResponse join(@ApiIgnore @CurrentUser Account account,
                               @PathVariable("idx") Long roomIdx) throws NotFoundException {
        roomService.join(account.getId(), roomIdx);
        return CommonResponse.success();
    }


    @ApiOperation(value = "스터디방 리스트")
    @GetMapping("/rooms")
    public CommonResponse<Page<RoomDto.Response>> rooms(RoomDto.SearchRequest searchRequest,
                                                            PageRequest pageRequest) throws NotFoundException {
        return CommonResponse.success(roomService.findAllWithPagination(searchRequest, pageRequest.of()));
    }


    @ApiOperation(value = "유저가 가입한 스터디방 리스트")
    @GetMapping("/user/rooms")
    public CommonResponse<Page<RoomDto.Response>> userRooms(@ApiIgnore @CurrentUser Account account,
                                                                RoomDto.SearchRequest searchRequest,
                                                                PageRequest pageRequest) throws NotFoundException {
        return CommonResponse.success(roomService.findAllByUserIdWithPagination(account.getId(), searchRequest, pageRequest.of()));
    }

    @ApiOperation(value = "카테고리 별 스터디방 리스트")
    @GetMapping("/category/{id}/rooms")
    public CommonResponse<Page<RoomDto.Response>> categoryRooms(RoomDto.SearchRequest searchRequest,
                                                                PageRequest pageRequest,
                                                                @PathVariable("id") Long categoryId) throws NotFoundException {
        return CommonResponse.success(roomService.findAllByCategoryIdWithPagination(categoryId, searchRequest, pageRequest.of()));
    }


    @ApiOperation(value = "스터디방 상세 보기")
    @GetMapping("/room/{id}")
    public CommonResponse<RoomDto.DetailResponse> room(@ApiIgnore @CurrentUser Account account,
                                                           @PathVariable("id") Long roomId) throws NotFoundException {
        return CommonResponse.success(new RoomDto.DetailResponse(
                        roomService.findDetail(roomId), roomMemberService.findCurrentUserStatus(roomId, account.getId())));
    }


}
