package switus.user.back.studywithus.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import switus.user.back.studywithus.common.annotaion.CurrentUser;
import switus.user.back.studywithus.domain.account.Account;
import switus.user.back.studywithus.domain.post.PostComment;
import switus.user.back.studywithus.dto.PostCommentDto;
import switus.user.back.studywithus.dto.PostDto;
import switus.user.back.studywithus.dto.common.CommonResponse;
import switus.user.back.studywithus.dto.common.CurrentAccount;
import switus.user.back.studywithus.dto.common.PageRequest;
import switus.user.back.studywithus.service.PostCommentService;

import javax.validation.Valid;
import java.util.List;

@Api(tags = {"Post Comment"})
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PostCommentApiController {

    private final PostCommentService postCommentService;


    @ApiOperation("댓글 작성")
    @PostMapping("/post/{postId}/comment")
    public CommonResponse create(@ApiIgnore @CurrentUser CurrentAccount account,
                               @Valid @RequestBody PostCommentDto.SaveRequest request,
                               @PathVariable("postId") Long postId) {
        PostComment postComment = postCommentService.create(account.getId(), postId, request);

        return CommonResponse.success(new PostCommentDto.Response(postComment, account.getId()));
    }

    @ApiOperation("댓글 리스트")
    @GetMapping("/post/{postId}/comments")
    public CommonResponse posts(@ApiIgnore @CurrentUser CurrentAccount account,
                                @PathVariable("postId") Long postId) {
        List<PostComment> postComments = postCommentService.findByPost(postId);
        return CommonResponse.success(postComments.stream().map(comment -> new PostCommentDto.Response(comment, account.getId())));
    }


    @ApiOperation("댓글 수정")
    @PutMapping("/comment/{commentId}")
    public CommonResponse update(@ApiIgnore @CurrentUser CurrentAccount account,
                                 @RequestBody PostCommentDto.UpdateRequest request,
                                 @PathVariable("commentId") Long commentId) {
        postCommentService.update(account.getId(), commentId, request);
        return CommonResponse.success();
    }


    @ApiOperation("댓글 삭제")
    @DeleteMapping("/comment/{commentId}")
    public CommonResponse delete(@ApiIgnore @CurrentUser CurrentAccount account,
                                 @PathVariable("commentId") Long commentId) {
        postCommentService.delete(account.getId(), commentId);
        return CommonResponse.success();
    }

}
