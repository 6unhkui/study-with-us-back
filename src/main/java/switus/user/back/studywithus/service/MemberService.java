package switus.user.back.studywithus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import switus.user.back.studywithus.common.error.exception.BadRequestException;
import switus.user.back.studywithus.common.error.exception.DuplicateEntryException;
import switus.user.back.studywithus.common.error.exception.NoContentException;
import switus.user.back.studywithus.common.error.exception.UnauthorizedException;
import switus.user.back.studywithus.domain.account.Account;
import switus.user.back.studywithus.domain.member.Member;
import switus.user.back.studywithus.domain.member.MemberRole;
import switus.user.back.studywithus.domain.post.Post;
import switus.user.back.studywithus.domain.room.Room;
import switus.user.back.studywithus.dto.MemberDto;
import switus.user.back.studywithus.repository.member.MemberRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final AccountService accountService;
    private final RoomService roomService;


    public Member findDetail(Long roomId){
        return Optional.ofNullable(memberRepository.findDetail(roomId)).orElseThrow(() -> new NoContentException("존재하지 않는 멤버입니다."));
    }

    public Optional<Member> findByAccountAndRoom(Long accountId, Long roomId){
        return Optional.ofNullable(memberRepository.findMembership(accountId, roomId));
    }

    public Member findMembership(Long accountId, Long roomId){
        return findByAccountAndRoom(accountId, roomId).orElseThrow(() -> new NoContentException("존재하지 않는 멤버입니다."));
    }


    public Member findManagerByRoomId(Long roomId) {
        return memberRepository.findManagerByRoomId(roomId);
    }

    public Page<MemberDto.Response> findMembers(Long roomId, MemberDto.SearchRequest searchRequest, Pageable pageable) {
       return memberRepository.findMembers(roomId, searchRequest, pageable);
    }

    @Transactional
    public void join(Long accountId, Long roomId) {
        findByAccountAndRoom(accountId, roomId).ifPresent(member -> {
            throw new DuplicateEntryException("이미 존재하는 멤버입니다.");
        });

        Account account = accountService.findById(accountId);
        Room room = roomService.findById(roomId);

        if(room.getMaxCount() != 0 && room.getJoinCount() == room.getMaxCount()){
            throw new BadRequestException("원수 초과로 더이상 가입 할 수 없는 스터디방입니다.");
        }

        Member member = Member.join(account, room, MemberRole.MATE);
        memberRepository.save(member);
    }


    @Transactional
    public void withdrawal(Long accountId, Long roomId) {
        Member member = findMembership(accountId, roomId);

        if(member.getRole() == MemberRole.MANAGER) {
            throw new BadRequestException("매니저는 탈퇴를 진행 할 수 없습니다.");
        }

        member.withdrawal();
    }


    @Transactional
    public void delete(Long memberId, Long currentAccountId) {
        Member member = Optional.ofNullable(memberRepository.findMembership(memberId)).orElseThrow(() -> new NoContentException("존재하지 않는 멤버입니다."));

        Member currentAccountMembership = findMembership(currentAccountId, member.getRoom().getId());
        if(currentAccountMembership.getRole() != MemberRole.MANAGER) {
            throw new BadRequestException("멤버 삭제는 매니저만 진행 할 수 있습니다.");
        }

        member.withdrawal();
    }

}
