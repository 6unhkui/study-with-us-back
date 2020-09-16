package switus.user.back.studywithus.domain.member;

import lombok.AllArgsConstructor;
import switus.user.back.studywithus.domain.common.BaseEnumCode;

@AllArgsConstructor
public enum RoomMemberRole implements BaseEnumCode<Integer> {
    MATE(0), MANAGER(99);

    private final Integer value;

    @Override
    public Integer getValue() {
        return value;
    }
}
