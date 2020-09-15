package switus.user.back.studywithus.repository.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import switus.user.back.studywithus.domain.member.RoomMember;
import switus.user.back.studywithus.domain.member.RoomMemberRole;

import static switus.user.back.studywithus.domain.member.QRoomMember.roomMember;
import static switus.user.back.studywithus.domain.room.QRoom.room;
import static switus.user.back.studywithus.domain.account.QAccount.account;

@RequiredArgsConstructor
public class RoomMemberRepositoryImpl implements RoomMemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public RoomMember findCurrentUserStatus(Long roomIdx, Long userIdx) {
        return queryFactory.selectFrom(roomMember)
                            .join(roomMember.account, account).on(roomMember.account.id.eq(userIdx))
                            .join(roomMember.room, room).on(roomMember.room.id.eq(roomIdx))
                            .fetchOne();
    }

    @Override
    public RoomMember findManagerByRoomId(Long roomIdx) {
        return queryFactory.selectFrom(roomMember)
                           .join(roomMember.account, account).fetchJoin()
                           .where(roomMember.room.id.eq(roomIdx), roomMember.role.eq(RoomMemberRole.MANAGER))
                           .fetchOne();
    }

    @Override
    public long countByRoomId(Long roomIdx) {
        return queryFactory.selectFrom(roomMember)
                            .join(roomMember.account, account)
                            .where(roomMember.room.id.eq(roomIdx)).fetchCount();
    }
}
