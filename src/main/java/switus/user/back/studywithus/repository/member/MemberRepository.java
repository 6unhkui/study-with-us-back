package switus.user.back.studywithus.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import switus.user.back.studywithus.domain.member.Member;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
//    @Query(value = "SELECT * " +
//                    "FROM (SELECT * FROM room_member WHERE room_idx = :roomIdx) rm " +
//                    "LEFT OUTER JOIN user u ON rm.user_idx = u.idx"
//            ,
//            nativeQuery = true)
//    List<Member> findByRoomIdx(@Param("roomIdx") Long roomIdx);
}
