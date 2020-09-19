package switus.user.back.studywithus.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;
import switus.user.back.studywithus.domain.post.Post;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
}
