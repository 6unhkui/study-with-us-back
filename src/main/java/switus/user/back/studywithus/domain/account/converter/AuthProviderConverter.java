package switus.user.back.studywithus.domain.account.converter;

import switus.user.back.studywithus.domain.account.AuthProvider;
import switus.user.back.studywithus.domain.common.AbstractBaseEnumConverter;

import javax.persistence.Converter;

@Converter
public class AuthProviderConverter extends AbstractBaseEnumConverter<AuthProvider, Character> {
    @Override
    protected AuthProvider[] getValueList() {
        return AuthProvider.values();
    }
}