package switus.user.back.studywithus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import switus.user.back.studywithus.common.error.exception.NoContentException;
import switus.user.back.studywithus.common.error.exception.UnauthorizedException;
import switus.user.back.studywithus.common.util.MultilingualMessageUtils;
import switus.user.back.studywithus.domain.member.Member;
import switus.user.back.studywithus.domain.post.Post;
import switus.user.back.studywithus.domain.post.PostComment;
import switus.user.back.studywithus.dto.PostCommentDto;
import switus.user.back.studywithus.repository.postComment.PostCommentRepository;

import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostCommentService {

    private final MemberService memberService;
    private final PostService postService;
    private final PostCommentRepository postCommentRepository;
    private final MultilingualMessageUtils message;

    public List<PostComment> findByPost(Long postId) {
        return postCommentRepository.findByPost(postId);
    }

    @Transactional
    public PostComment create(Long accountId, Long postId, PostCommentDto.SaveRequest request) {
        PostComment postComment = request.toEntity();

        Post post = postService.findById(postId);
        Member member = memberService.findMembership(accountId, post.getMember().getRoom().getId());

        postComment.setWriter(member);
        postComment.setPost(post);

        if(null != request.getParentId()) {
            PostComment parent = findById(request.getParentId());
            postComment.setParent(parent);

            PostComment maxSeq = postCommentRepository.findMaxSeqByParent(request.getParentId());
            postComment.setSeq(nextSeq(maxSeq));
        }else {
            PostComment maxSeq = postCommentRepository.findMaxSeq();
            postComment.setSeq(nextSeq(maxSeq));
        }

        return postCommentRepository.save(postComment);
    }

    public int nextSeq(PostComment postComment) {
        try {
            return postComment.getSeq() + 1;
        }catch (NullPointerException e) {
            return 1;
        }
    }


    public PostComment findById(Long id) {
       return postCommentRepository.findById(id)
               .orElseThrow(() -> new NoContentException(message.makeMultilingualMessage("comment.notExist")));
    }


    @Transactional
    public void update(Long accountId, Long commentId, PostCommentDto.UpdateRequest request) {
        PostComment postComment = findById(commentId);
        validateResourcePermission(postComment, accountId);
        postComment.change(request.getContent());
    }


    @Transactional
    public void delete(Long accountId, Long commentId) {
        PostComment postComment = findById(commentId);
        validateResourcePermission(postComment, accountId);
        postComment.delete();
    }


    public void validateResourcePermission(PostComment postComment, Long accountId) {
        Member manager = memberService.findManagerByRoomId(postComment.getMember().getRoom().getId());

        // 작성자거나 매니저가 아닌 경우 리소스 접근 권한이 없다.
        if(!postComment.getMember().getAccount().getId().equals(accountId)
           && !manager.getId().equals(accountId)) {
            throw new UnauthorizedException("잘못된 권한의 요청입니다.");
        }
    }

}
