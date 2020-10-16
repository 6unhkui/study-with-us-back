package switus.user.back.studywithus.repository.postComment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import switus.user.back.studywithus.domain.post.PostComment;

public interface PostCommentRepository extends JpaRepository<PostComment, Long>, PostCommentRepositoryCustom {
}
