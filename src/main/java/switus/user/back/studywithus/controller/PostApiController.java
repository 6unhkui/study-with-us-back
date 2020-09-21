package switus.user.back.studywithus.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import switus.user.back.studywithus.common.annotaion.CurrentUser;
import switus.user.back.studywithus.domain.account.Account;
import switus.user.back.studywithus.dto.PostDto;
import switus.user.back.studywithus.dto.common.CommonResponse;
import switus.user.back.studywithus.dto.common.PageRequest;
import switus.user.back.studywithus.service.PostService;

@Api(tags = {"5. Post"})
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PostApiController {

    private final PostService postService;


    @ApiOperation("게시글 작성")
    @PostMapping("/room/{roomId}/post")
    public CommonResponse create(@ApiIgnore @CurrentUser Account account,
                                 PostDto.SaveRequest request,
                                 @PathVariable("roomId") Long roomId) {
        postService.create(account.getId(), roomId, request);
        return CommonResponse.success();
    }


    @ApiOperation("게시글 리스트")
    @GetMapping("/room/{roomId}/posts")
    public CommonResponse posts(@PathVariable("roomId") Long roomId,
                                PageRequest pageRequest) {
        return CommonResponse.success(postService.findAll(roomId, pageRequest.of()).map(PostDto.Response::new));
    }


    @ApiOperation("게시글 상세보기")
    @GetMapping("/post/{postId}")
    public CommonResponse post(@PathVariable("postId") Long postId) {
        return CommonResponse.success(new PostDto.Response(postService.findById(postId)));
    }



}
