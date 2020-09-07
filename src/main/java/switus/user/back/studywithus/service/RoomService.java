package switus.user.back.studywithus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import switus.user.back.studywithus.common.error.exception.NoContentException;
import switus.user.back.studywithus.domain.member.RoomMember;
import switus.user.back.studywithus.domain.member.RoomMemberRole;
import switus.user.back.studywithus.domain.room.Room;
import switus.user.back.studywithus.domain.user.User;
import switus.user.back.studywithus.dto.RoomDto;
import switus.user.back.studywithus.repository.RoomMemberRepository;
import switus.user.back.studywithus.repository.RoomRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final UserService userService;

    @Transactional
    public Long create(Long userIdx, RoomDto.SaveRequest request) {
        Room room = request.toEntity();
        roomRepository.save(room);

        User user = userService.findByIdx(userIdx);
        RoomMember roomMember = RoomMember.join(user, room, RoomMemberRole.MANAGER);

        return roomMemberRepository.save(roomMember).getIdx();
    }


    public Page<RoomDto.RoomResponse> findByUserIdx(Long userIdx, RoomDto.SearchRequest searchRequest, Pageable pageable) {
        return roomRepository.findByUserIdx(userIdx, searchRequest, pageable).map((RoomDto.RoomResponse::new));
    }

    public RoomDto.RoomDetailResponse findByIdx(Long idx) {
        return roomRepository.findById(idx).map(RoomDto.RoomDetailResponse::new)
                                            .orElseThrow(() -> new NoContentException("존재하지 않는 스터디방입니다."));
    }
}
