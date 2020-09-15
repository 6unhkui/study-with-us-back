package switus.user.back.studywithus.repository.member;


import switus.user.back.studywithus.domain.member.RoomMember;

public interface RoomMemberRepositoryCustom {
    RoomMember findCurrentUserStatus(Long roomId, Long userId);
    RoomMember findManagerByRoomId(Long roomId);
    long countByRoomId(Long roomId);
}
