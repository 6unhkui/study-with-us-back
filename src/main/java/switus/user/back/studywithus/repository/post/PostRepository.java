package switus.user.back.studywithus.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import switus.user.back.studywithus.domain.post.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    @Query("SELECT count(id) FROM Post p WHERE p.room.id =:roomId")
    Long countByRoom(@Param("roomId") Long roomId);

    @Query("SELECT count(id) FROM Post p WHERE p.member.id =:memberId")
    Long countByMember(@Param("memberId") Long memberId);

    @Modifying
    @Query("UPDATE Post p SET p.delFlag = true WHERE p.room.id =:roomId")
    void deleteAllByRoom(@Param("roomId") Long roomId);
}
