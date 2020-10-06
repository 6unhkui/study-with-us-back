package switus.user.back.studywithus.repository.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import switus.user.back.studywithus.domain.post.Post;
import switus.user.back.studywithus.dto.PostDto;

public interface PostRepositoryCustom {
    Page<Post> findAllByRoom(Long roomId, PostDto.SearchRequest searchRequest, Pageable pageable);
    Page<Post> findAllByJoinedRoomOrderByInsDate(Long accountId, Pageable pageable);
    Post findDetail(Long postId);
}
