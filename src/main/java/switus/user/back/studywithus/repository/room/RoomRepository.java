package switus.user.back.studywithus.repository.room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import switus.user.back.studywithus.domain.room.Room;

public interface RoomRepository extends JpaRepository<Room, Long>, RoomRepositoryCustom {
}
