package switus.user.back.studywithus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import switus.user.back.studywithus.common.error.exception.NoContentException;
import switus.user.back.studywithus.domain.file.FileGroup;
import switus.user.back.studywithus.domain.member.Member;
import switus.user.back.studywithus.domain.post.Post;
import switus.user.back.studywithus.dto.PostDto;
import switus.user.back.studywithus.repository.post.PostRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final AccountService accountService;
    private final RoomService roomService;
    private final MemberService memberService;
    private final FileService fileService;


    @Transactional
    public Long create(Long accountId, Long roomId, PostDto.SaveRequest request) {
        Post post = request.toEntity();

        // 첨부 파일
        if(null != request.getFileGroupId()) {
            FileGroup fileGroup = fileService.findFileGroup(request.getFileGroupId());
            post.setFileGroup(fileGroup);
        }

        Member member = memberService.findMembership(accountId, roomId);
        post.setAuthor(member);

        return postRepository.save(post).getId();
    }

    public Page<Post> findAll(Long roomId,PostDto.SearchRequest searchRequest, Pageable pageable) {
        return postRepository.findAll(roomId, searchRequest, pageable);
    }

    public Post findById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new NoContentException("존재하지 않는 게시물입니다."));
    }


}
