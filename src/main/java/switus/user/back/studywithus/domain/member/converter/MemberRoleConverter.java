package switus.user.back.studywithus.domain.member.converter;

import switus.user.back.studywithus.domain.common.AbstractBaseEnumConverter;
import switus.user.back.studywithus.domain.member.MemberRole;

import javax.persistence.Converter;

@Converter
public class MemberRoleConverter extends AbstractBaseEnumConverter<MemberRole, Integer> {
    @Override
    protected MemberRole[] getValueList() {
        return MemberRole.values();
    }
}