package switus.user.back.studywithus.domain.member.converter;

import switus.user.back.studywithus.domain.member.RoomMemberRole;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

@Convert
public class RoomMemberRoleConverter implements AttributeConverter<RoomMemberRole, Integer> {

    @Override
    public Integer convertToDatabaseColumn(RoomMemberRole attribute) {
        if(attribute == null) return null;
        return attribute.getValue();
    }

    @Override
    public RoomMemberRole convertToEntityAttribute(Integer dbData) {
        if(dbData == null) return null;
        return RoomMemberRole.findByValue(dbData);
    }
}