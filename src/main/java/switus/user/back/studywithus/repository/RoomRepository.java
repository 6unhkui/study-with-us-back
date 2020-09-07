package switus.user.back.studywithus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import switus.user.back.studywithus.domain.room.Room;

public interface RoomRepository extends JpaRepository<Room, Long>, RoomRepositoryCustom {
}
