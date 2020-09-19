package switus.user.back.studywithus.repository.member;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import switus.user.back.studywithus.domain.member.Member;
import switus.user.back.studywithus.dto.MemberDto;

public interface MemberRepositoryCustom {
    Member findMembership(Long accountId, Long roomId);
    Member findManagerByRoomId(Long roomId);
    Page<Member> findMembers(Long roomId, MemberDto.SearchRequest searchRequest, Pageable pageable);
}
