package switus.user.back.studywithus.repository.room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import switus.user.back.studywithus.domain.room.Room;

public interface RoomRepository extends JpaRepository<Room, Long>, RoomRepositoryCustom {
    //    @Query(value = "SELECT * " +
//                    "FROM (SELECT * FROM room_member WHERE room_idx = :roomIdx) rm " +
//                    "LEFT OUTER JOIN user u ON rm.user_idx = u.idx"
//            ,
//            nativeQuery = true)
//    List<RoomMember> findByRoomIdx(@Param("roomIdx") Long roomIdx);

//    @Modifying
//    @Query(value = "UPDATE Room r SET r.joinCount = r.joinCount+1 WHERE r.idx=:idx")
//    int incrementJoinCount();
//
//    @Modifying
//    @Query(value = "update Room r set r.joinCount = r.joinCount-1 where r.idx=:idx")
//    int decrementJoinCount();
}
