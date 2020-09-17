package switus.user.back.studywithus.repository.member;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import switus.user.back.studywithus.domain.member.RoomMember;
import switus.user.back.studywithus.dto.RoomMemberDto;

public interface RoomMemberRepositoryCustom {
    RoomMember findMembership(Long accountId, Long roomId);
    RoomMember findManagerByRoomId(Long roomId);
    Page<RoomMember> findMembers(Long roomId, RoomMemberDto.SearchRequest searchRequest, Pageable pageable);
}
