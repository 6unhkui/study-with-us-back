package switus.user.back.studywithus.domain.template.converter;

import switus.user.back.studywithus.domain.common.AbstractBaseEnumConverter;
import switus.user.back.studywithus.domain.template.SendType;

import javax.persistence.Converter;

@Converter
public class SendTypeConverter extends AbstractBaseEnumConverter<SendType, Character> {
    @Override
    protected SendType[] getValueList() {
        return SendType.values();
    }
}
