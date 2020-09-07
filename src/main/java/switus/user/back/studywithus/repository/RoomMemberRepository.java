package switus.user.back.studywithus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import switus.user.back.studywithus.domain.member.RoomMember;
import switus.user.back.studywithus.domain.user.User;

import java.util.List;

public interface RoomMemberRepository extends JpaRepository<RoomMember, Long> {
    List<RoomMember> findByUser(User user);
}
