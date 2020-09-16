package switus.user.back.studywithus.domain.member.converter;

import switus.user.back.studywithus.domain.common.AbstractBaseEnumConverter;
import switus.user.back.studywithus.domain.member.RoomMemberRole;

import javax.persistence.Converter;

@Converter
public class RoomMemberRoleConverter extends AbstractBaseEnumConverter<RoomMemberRole, Integer> {
    @Override
    protected RoomMemberRole[] getValueList() {
        return RoomMemberRole.values();
    }
}