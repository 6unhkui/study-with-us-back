package switus.user.back.studywithus.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import switus.user.back.studywithus.common.annotaion.CurrentUser;
import switus.user.back.studywithus.domain.account.Account;
import switus.user.back.studywithus.dto.PostCommentDto;
import switus.user.back.studywithus.dto.PostDto;
import switus.user.back.studywithus.dto.common.CommonResponse;
import switus.user.back.studywithus.dto.common.PageRequest;
import switus.user.back.studywithus.service.PostCommentService;

@Api(tags = {"6. Post Comment"})
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PostCommentApiController {

    private final PostCommentService postCommentService;


    @ApiOperation("댓글 작성")
    @PostMapping("/post/{postId}/comment")
    public CommonResponse create(@ApiIgnore @CurrentUser Account account,
                                 PostCommentDto.SaveRequest request,
                                 @PathVariable("postId") Long postId) {

        return CommonResponse.success();
    }


    @ApiOperation("댓글 리스트")
    @GetMapping("/post/{postId}/comments")
    public CommonResponse posts(@PathVariable("postId") Long postId,
                                PageRequest pageRequest) {
        return CommonResponse.success(postCommentService.findByPost(postId, pageRequest.of()).map(PostCommentDto.Response::new));
    }
}
