package switus.user.back.studywithus.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import switus.user.back.studywithus.common.annotaion.CurrentUser;
import switus.user.back.studywithus.domain.account.Account;
import switus.user.back.studywithus.dto.RoomPostDto;
import switus.user.back.studywithus.dto.common.CommonResponse;
import switus.user.back.studywithus.dto.common.PageRequest;
import switus.user.back.studywithus.service.RoomPostService;

@Api(tags = {"5. Room Post"})
@RestController
@RequestMapping(value = "/api/v1/room", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class RoomPostApiController {

    private final RoomPostService roomPostService;

    @ApiOperation("게시글 작성")
    @PostMapping("/{roomId}/post")
    public CommonResponse create(@ApiIgnore @CurrentUser Account account,
                                 RoomPostDto.SaveRequest request,
                                 @PathVariable("roomId") Long roomId) {
        System.out.println(request.toString());
        roomPostService.create(account.getId(), roomId, request);
        return CommonResponse.success();
    }

    @ApiOperation("게시글 리스트")
    @GetMapping("/{roomId}/posts")
    public CommonResponse posts(@PathVariable("roomId") Long roomId,
                                PageRequest pageRequest) {
        return CommonResponse.success(roomPostService.findAll(roomId, pageRequest.of()).map(RoomPostDto.Response::new));
    }


    @ApiOperation("게시글 리스트")
    @GetMapping("/{roomId}/post/{postId}")
    public CommonResponse post(@PathVariable("roomId") Long roomId,
                               @PathVariable("postId") Long postId,
                               PageRequest pageRequest) {
        return CommonResponse.success(new RoomPostDto.Response(roomPostService.findById(roomId)));
    }



}
