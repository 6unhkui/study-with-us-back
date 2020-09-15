package switus.user.back.studywithus.domain.account.converter;

import switus.user.back.studywithus.domain.account.AuthProvider;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class AuthProviderConverter implements AttributeConverter<AuthProvider, Integer> {
    @Override
    public Integer convertToDatabaseColumn(AuthProvider attribute) {
        if(attribute == null) return null;
        return attribute.getValue();
    }

    @Override
    public AuthProvider convertToEntityAttribute(Integer dbData) {
        if(dbData == null) return null;
        return AuthProvider.findByValue(dbData);
    }
}
