package switus.user.back.studywithus.repository.room;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import switus.user.back.studywithus.domain.room.Room;
import switus.user.back.studywithus.dto.RoomDto;

public interface RoomRepositoryCustom {
    Page<Room> findAll(RoomDto.SearchRequest searchRequest, Pageable pageable);
    Page<Room> findAllByAccountId(Long accountIdx, RoomDto.SearchRequest searchRequest, Pageable pageable);
    Room findDetailById(Long idx);
}
