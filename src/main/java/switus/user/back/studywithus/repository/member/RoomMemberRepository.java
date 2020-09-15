package switus.user.back.studywithus.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import switus.user.back.studywithus.domain.member.RoomMember;

import java.util.List;

public interface RoomMemberRepository extends JpaRepository<RoomMember, Long>, RoomMemberRepositoryCustom {
//    @Query(value = "SELECT * " +
//                    "FROM (SELECT * FROM room_member WHERE room_idx = :roomIdx) rm " +
//                    "LEFT OUTER JOIN user u ON rm.user_idx = u.idx"
//            ,
//            nativeQuery = true)
//    List<RoomMember> findByRoomIdx(@Param("roomIdx") Long roomIdx);
}
