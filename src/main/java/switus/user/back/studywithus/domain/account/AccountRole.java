package switus.user.back.studywithus.domain.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import switus.user.back.studywithus.domain.common.BaseEnumCode;

@Getter
@AllArgsConstructor
public enum AccountRole implements BaseEnumCode<Integer>{
    USER(0), ADMIN(99);

    private final Integer value;

    @Override
    public Integer getValue() {
        return value;
    }
}
