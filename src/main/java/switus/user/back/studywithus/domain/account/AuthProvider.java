package switus.user.back.studywithus.domain.account;


import java.util.Arrays;

public enum AuthProvider {
    LOCAL(0), GOOGLE(1), NAVER(2);

    private final int value;

    AuthProvider(int value) {
        this.value = value;
    }

    public static AuthProvider findByValue(int value){
        return Arrays.stream(AuthProvider.values())
                .filter(v -> v.getValue() == value)
                .findAny().orElse(null);
    }

    public int getValue() {
        return this.value;
    }
}
