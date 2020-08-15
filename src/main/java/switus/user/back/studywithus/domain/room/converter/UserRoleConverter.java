package switus.user.back.studywithus.domain.room.converter;

import switus.user.back.studywithus.domain.room.UserRole;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

@Convert
public class UserRoleConverter implements AttributeConverter<UserRole, Integer> {

    @Override
    public Integer convertToDatabaseColumn(UserRole attribute) {
        if(attribute == null) return null;
        return attribute.getValue();
    }

    @Override
    public UserRole convertToEntityAttribute(Integer dbData) {
        if(dbData == null) return null;
        return UserRole.findByValue(dbData);
    }
}