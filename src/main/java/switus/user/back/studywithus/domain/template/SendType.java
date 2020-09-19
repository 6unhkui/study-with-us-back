package switus.user.back.studywithus.domain.template;

import lombok.AllArgsConstructor;
import lombok.Getter;
import switus.user.back.studywithus.domain.common.BaseEnumCode;

@Getter
@AllArgsConstructor
public enum SendType implements BaseEnumCode<Character> {
    REGISTER('R'), FORGOT_PW('F');

    private final Character value;

    @Override
    public Character getValue() {
        return this.value;
    }
}
