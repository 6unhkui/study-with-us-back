package switus.user.back.studywithus.domain.file;

import lombok.AllArgsConstructor;
import switus.user.back.studywithus.domain.common.BaseEnumCode;

@AllArgsConstructor
public enum FileType implements BaseEnumCode<Integer> {
    COVER(0), EDITOR(1), ATTACHMENT(2);

    private final Integer value;

    @Override
    public Integer getValue() {
        return value;
    }
}
