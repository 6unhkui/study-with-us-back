package switus.user.back.studywithus.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import switus.user.back.studywithus.domain.room.Room;
import switus.user.back.studywithus.dto.RoomDto;

public interface RoomRepositoryCustom {
    Page<Room> findByUserIdx(Long userIdx, RoomDto.SearchRequest searchRequest, Pageable pageable);
}
