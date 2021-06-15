package switus.user.back.studywithus.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import switus.user.back.studywithus.common.annotaion.CurrentUser;
import switus.user.back.studywithus.domain.account.Account;
import switus.user.back.studywithus.domain.post.Post;
import switus.user.back.studywithus.dto.MemberDto;
import switus.user.back.studywithus.dto.PostDto;
import switus.user.back.studywithus.dto.common.CommonResponse;
import switus.user.back.studywithus.dto.common.CurrentAccount;
import switus.user.back.studywithus.dto.common.PageRequest;
import switus.user.back.studywithus.service.PostService;

import java.util.Arrays;

@Api(tags = {"Post"})
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PostApiController {

    private final PostService postService;


    @ApiOperation("게시글 작성")
    @PostMapping("/room/{roomId}/post")
    public CommonResponse create(@ApiIgnore @CurrentUser CurrentAccount account,
                                 @RequestBody PostDto.SaveRequest request,
                                 @PathVariable("roomId") Long roomId) {
        return CommonResponse.success(postService.create(account.getId(), roomId, request));
    }


    @ApiOperation("게시글 리스트")
    @GetMapping("/room/{roomId}/posts")
    public CommonResponse posts(@PathVariable("roomId") Long roomId,
                                PostDto.SearchRequest searchRequest,
                                PageRequest pageRequest) {
        Page<Post> posts = postService.findAllByRoom(roomId, searchRequest, pageRequest.of());
        return CommonResponse.success(posts.map(PostDto.Response::new));
    }


    @ApiOperation("게시글 상세보기")
    @GetMapping("/post/{postId}")
    public CommonResponse<PostDto.DetailResponse> detail(@ApiIgnore @CurrentUser CurrentAccount account,
                                                         @PathVariable("postId") Long postId) {
        Post post = postService.findById(postId);
        return CommonResponse.success(new PostDto.DetailResponse(post, account.getId()));
    }


    @ApiOperation("게시글 수정")
    @PutMapping("/post/{postId}")
    public CommonResponse<?> update(@ApiIgnore @CurrentUser CurrentAccount account,
                                 @RequestBody PostDto.UpdateRequest request,
                                 @PathVariable("postId") Long postId) {
        postService.update(account.getId(), postId, request);
        return CommonResponse.success();
    }


    @ApiOperation("게시글 삭제")
    @DeleteMapping("/post/{postId}")
    public CommonResponse<?> delete(@ApiIgnore @CurrentUser CurrentAccount account,
                                 @PathVariable("postId") Long postId) {
        postService.delete(account.getId(), postId);
        return CommonResponse.success();
    }


    @ApiOperation("내가 가입한 스터디룸의 새 게시글 리스트")
    @GetMapping("/posts/new")
    public CommonResponse newsFeed(@ApiIgnore @CurrentUser CurrentAccount account,
                                   PageRequest pageRequest) {
        Page<Post> posts = postService.findAllByJoinedRoomOrderByInsDate(account.getId(), pageRequest.of());
        return CommonResponse.success(posts.map(post -> {
            PostDto.Response response = new PostDto.Response(post);
            response.setRoomName(post.getRoom().getName());
            return response;
        }));
    }

}
