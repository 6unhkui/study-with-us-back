package switus.user.back.studywithus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import switus.user.back.studywithus.common.error.exception.BadRequestException;
import switus.user.back.studywithus.common.error.exception.NoContentException;
import switus.user.back.studywithus.common.error.exception.UnauthorizedException;
import switus.user.back.studywithus.domain.file.FileGroup;
import switus.user.back.studywithus.domain.member.Member;
import switus.user.back.studywithus.domain.post.Post;
import switus.user.back.studywithus.domain.post.PostComment;
import switus.user.back.studywithus.dto.PostDto;
import switus.user.back.studywithus.repository.post.PostRepository;

import java.util.Arrays;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberService memberService;
    private final FileService fileService;


    @Transactional
    public Long save(Long accountId, Long roomId, PostDto.SaveRequest request) {
        Post post = request.toEntity();

        // 첨부 파일이 존재할 경우
        if(null != request.getFileGroupId()) {
            FileGroup fileGroup = fileService.findFileGroup(request.getFileGroupId());
            post.setFileGroup(fileGroup);
        }

        Member member = memberService.findMembership(accountId, roomId);
        post.setWriter(member);

        return postRepository.save(post).getId();
    }

    public Page<Post> findAll(Long roomId,PostDto.SearchRequest searchRequest, Pageable pageable) {
        return postRepository.findAll(roomId, searchRequest, pageable);
    }

    public Post findById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new NoContentException("존재하지 않는 게시물입니다."));
    }

    @Transactional
    public void delete(Long accountId, Long postId) {
        Post post = findById(postId);
        validateResourcePermission(post, accountId);
        post.delete();
    }

    @Transactional
    public void update(Long accountId, Long postId, PostDto.UpdateRequest request)  {
        Post post = findById(postId);
        validateResourcePermission(post, accountId);

        // 첨부 파일이 존재할 경우
        if(null != request.getFileGroupId()) {
            FileGroup fileGroup = fileService.findFileGroup(request.getFileGroupId());
            post.setFileGroup(fileGroup);
        }

        // 기존 첨부 파일 중 삭제된 파일이 존재할 경우
        if(null != request.getDelFiles() && request.getDelFiles().length > 0) {
            fileService.delete(request.getDelFiles());
        }

        post.editPost(request.getTitle(), request.getContent());
    }

    public void validateResourcePermission(Post post, Long accountId) {
        Member manager = memberService.findManagerByRoomId(post.getMember().getRoom().getId());

        // 작성자거나 매니저가 아닌 경우 리소스 접근 권한이 없다.
        if(!post.getMember().getAccount().getId().equals(accountId)
                && !manager.getId().equals(accountId)) {
            throw new UnauthorizedException("잘못된 권한의 요청입니다.");
        }
    }

}
