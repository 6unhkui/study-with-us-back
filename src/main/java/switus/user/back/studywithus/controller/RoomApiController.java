package switus.user.back.studywithus.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import switus.user.back.studywithus.common.security.CustomUserDetails;
import switus.user.back.studywithus.dto.common.CurrentUser;
import switus.user.back.studywithus.dto.RoomDto;
import switus.user.back.studywithus.dto.common.CommonResponse;
import switus.user.back.studywithus.dto.common.PageRequest;
import switus.user.back.studywithus.service.RoomService;

@Api(tags = {"3. Room"})
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class RoomApiController {

    private final RoomService roomService;


    @ApiOperation(value = "스터디방 만들기", notes = "스터디방을 만듭니다.")
    @PostMapping("/room")
    public CommonResponse create(@ApiIgnore @CurrentUser CustomUserDetails userDetails,
                                 @RequestBody RoomDto.SaveRequest request) {
        return CommonResponse.success(roomService.create(userDetails.getIdx(), request));
    }


    @ApiOperation(value = "내가 가입한 스터디방 리스트", notes = "내가 가입된 스터디방 리스트를 보여줍니다.")
    @GetMapping("/user/rooms")
    public CommonResponse<Page<RoomDto.RoomResponse>> userRooms(@ApiIgnore @CurrentUser CustomUserDetails userDetails,
                                                                RoomDto.SearchRequest searchRequest,
                                                                PageRequest pageRequest){
        return CommonResponse.success(roomService.findByUserIdx(userDetails.getIdx(), searchRequest, pageRequest.of()));
    }


    @ApiOperation(value = "스터디방 상세 보기", notes = "스터디방 상세 보기")
    @GetMapping("/room/{idx}")
    public CommonResponse<RoomDto.RoomDetailResponse> room(@ApiIgnore @CurrentUser CustomUserDetails userDetails,
                                                           @PathVariable("idx") Long idx){
        return CommonResponse.success(roomService.findByIdx(idx));
    }

}
