package switus.user.back.studywithus.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import switus.user.back.studywithus.common.annotaion.CurrentUser;
import switus.user.back.studywithus.domain.account.Account;
import switus.user.back.studywithus.dto.MemberDto;
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
    public CommonResponse write(@ApiIgnore @CurrentUser Account account,
                                 @RequestBody PostDto.SaveRequest request,
                                 @PathVariable("roomId") Long roomId) {
        postService.save(account.getId(), roomId, request);
        return CommonResponse.success();
    }


    @ApiOperation("게시글 리스트")
    @GetMapping("/room/{roomId}/posts")
    public CommonResponse posts(@PathVariable("roomId") Long roomId,
                                PostDto.SearchRequest searchRequest,
                                PageRequest pageRequest) {
        return CommonResponse.success(postService.findAll(roomId, searchRequest, pageRequest.of()).map(PostDto.Response::new));
    }


    @ApiOperation("게시글 상세보기")
    @GetMapping("/post/{postId}")
    public CommonResponse detail(@PathVariable("postId") Long postId) {
        return CommonResponse.success(new PostDto.DetailResponse(postService.findById(postId)));
    }


    @ApiOperation("게시글 수정")
    @PutMapping("/post/{postId}")
    public CommonResponse update(@ApiIgnore @CurrentUser Account account,
                                 @RequestBody PostDto.UpdateRequest request,
                                 @PathVariable("postId") Long postId) {
        postService.update(account.getId(), postId, request);
        return CommonResponse.success();
    }


    @ApiOperation("게시글 삭제")
    @DeleteMapping("/post/{postId}")
    public CommonResponse delete(@ApiIgnore @CurrentUser Account account,
                                 @PathVariable("postId") Long postId) {
        postService.delete(account.getId(), postId);
        return CommonResponse.success();
    }

}
