package switus.user.back.studywithus.domain.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import switus.user.back.studywithus.domain.common.BaseEnumCode;

@Getter
@AllArgsConstructor
public enum AuthProvider implements BaseEnumCode<Character>{
    LOCAL('L'), GOOGLE('G'), NAVER('N');

    private final Character value;

    @Override
    public Character getValue() {
        return this.value;
    }

}
