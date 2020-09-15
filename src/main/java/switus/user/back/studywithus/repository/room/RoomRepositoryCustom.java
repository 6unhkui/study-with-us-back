package switus.user.back.studywithus.repository.room;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import switus.user.back.studywithus.domain.member.RoomMember;
import switus.user.back.studywithus.domain.member.RoomMemberRole;
import switus.user.back.studywithus.domain.room.Room;
import switus.user.back.studywithus.dto.RoomDto;

public interface RoomRepositoryCustom {
    Page<Room> findAllWithPagination(RoomDto.SearchRequest searchRequest, Pageable pageable);
    Page<Room> findAllByUserIdWithPagination(Long userIdx, RoomDto.SearchRequest searchRequest, Pageable pageable);
    Page<Room> findAllByCategoryIdWithPagination(Long categoryIdx, RoomDto.SearchRequest searchRequest, Pageable pageable);
    Room findOneById(Long idx);

//    RoomMember findMemberRoleByUserIdx(Long userIdx, Long roomIdx);
//    RoomDto.DetailResponse findDetail(Long userIdx, Long RoomIdx);
}
