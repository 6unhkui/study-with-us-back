package switus.user.back.studywithus.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;
import switus.user.back.studywithus.domain.post.RoomPost;

public interface RoomPostRepository extends JpaRepository<RoomPost, Long>, RoomPostRepositoryCustom {
}
