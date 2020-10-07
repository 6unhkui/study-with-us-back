package switus.user.back.studywithus.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import switus.user.back.studywithus.domain.member.Member;


public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
}
