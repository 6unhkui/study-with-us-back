package switus.user.back.studywithus.domain.common;

import javax.persistence.AttributeConverter;
import java.util.Arrays;

public abstract class AbstractBaseEnumConverter<X extends Enum<X> & BaseEnumCode<Y>, Y> implements AttributeConverter<X, Y> {

    protected abstract X[] getValueList();

    @Override
    public Y convertToDatabaseColumn(X attribute) {
        if(attribute == null) return null;
        return attribute.getValue();
    }

    @Override
    public X convertToEntityAttribute(Y dbData) {
        if(dbData == null) return null;
        return Arrays.stream(getValueList())
                .filter(e -> e.getValue() == dbData || e.getValue().equals(dbData))
                .findFirst().orElseThrow(() -> new IllegalArgumentException(String.format("Unsupported type for %s.", dbData)));

    }
}
