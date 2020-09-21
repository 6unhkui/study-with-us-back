package switus.user.back.studywithus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import switus.user.back.studywithus.domain.post.Post;
import switus.user.back.studywithus.domain.post.PostComment;
import switus.user.back.studywithus.repository.postComment.PostCommentRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostCommentService {

    private final PostCommentRepository postCommentRepository;

    public Page<PostComment> findByPost(Long postId, Pageable pageable) {
        return postCommentRepository.findByPost(postId, pageable);
    }

}
