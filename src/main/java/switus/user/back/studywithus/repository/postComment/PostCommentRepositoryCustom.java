package switus.user.back.studywithus.repository.postComment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import switus.user.back.studywithus.domain.post.PostComment;

import java.util.List;

public interface PostCommentRepositoryCustom {
//    Page<PostComment> findByPost(Long postId, Pageable pageable);
    List<PostComment> findByPost(Long postId);
    PostComment findMaxSeqByParent(Long parentId);
    PostComment findMaxSeq();

}
