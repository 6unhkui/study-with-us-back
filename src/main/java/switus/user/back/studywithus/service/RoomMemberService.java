package switus.user.back.studywithus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import switus.user.back.studywithus.domain.member.RoomMember;
import switus.user.back.studywithus.repository.member.RoomMemberRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomMemberService {

    private final RoomMemberRepository roomMemberRepository;

    public RoomMember findCurrentUserStatus(Long roomId, Long userId) {
        return roomMemberRepository.findCurrentUserStatus(roomId, userId);
    }

    public RoomMember findManagerByRoomId(Long roomId) {
        return roomMemberRepository.findManagerByRoomId(roomId);
    }

}
