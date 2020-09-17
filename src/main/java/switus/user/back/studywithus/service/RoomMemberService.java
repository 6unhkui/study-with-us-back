package switus.user.back.studywithus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import switus.user.back.studywithus.common.error.exception.BadRequestException;
import switus.user.back.studywithus.common.error.exception.NoContentException;
import switus.user.back.studywithus.domain.account.Account;
import switus.user.back.studywithus.domain.member.RoomMember;
import switus.user.back.studywithus.domain.member.RoomMemberRole;
import switus.user.back.studywithus.domain.room.Room;
import switus.user.back.studywithus.dto.RoomMemberDto;
import switus.user.back.studywithus.repository.member.RoomMemberRepository;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomMemberService {

    private final RoomMemberRepository roomMemberRepository;

    private final AccountService accountService;
    private final RoomService roomService;


    public RoomMember findMembership(Long accountId, Long roomId){
        return roomMemberRepository.findMembership(accountId, roomId);
    }

    public RoomMember findManagerByRoomId(Long roomId) {
        return roomMemberRepository.findManagerByRoomId(roomId);
    }

    public Page<RoomMember> findMembers(Long roomId, RoomMemberDto.SearchRequest searchRequest, Pageable pageable) {
       return roomMemberRepository.findMembers(roomId, searchRequest, pageable);
    }


    @Transactional
    public void join(Long accountId, Long roomId) {
        Account account = accountService.findById(accountId);
        Room room = roomService.findById(roomId);

        if(room.getMaxCount() != 0 && room.getJoinCount() == room.getMaxCount()){
            throw new BadRequestException("원수 초과로 더이상 가입 할 수 없는 스터디방입니다.");
        }

        RoomMember roomMember = RoomMember.join(account, room, RoomMemberRole.MATE);
        roomMemberRepository.save(roomMember);
    }


    @Transactional
    public void deleteMember(Long accountId, Long roomIdx) {
        RoomMember roomMember = Optional.of(findMembership(accountId, roomIdx)).orElseThrow(() ->
            new NoContentException("존재하지 않는 멤버입니다.")
        );

        if(roomMember.getRole() == RoomMemberRole.MANAGER) {
            throw new BadRequestException("매니저는 탈퇴를 진행 할 수 없습니다.");
        }

        roomMember.deleteMember();
    }

}
