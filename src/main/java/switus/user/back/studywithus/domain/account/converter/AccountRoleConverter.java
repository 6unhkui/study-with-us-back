package switus.user.back.studywithus.domain.account.converter;

import switus.user.back.studywithus.domain.account.AccountRole;
import switus.user.back.studywithus.domain.common.AbstractBaseEnumConverter;

import javax.persistence.Converter;

@Converter
public class AccountRoleConverter extends AbstractBaseEnumConverter<AccountRole, Integer> {
    @Override
    protected AccountRole[] getValueList() {
        return AccountRole.values();
    }
}