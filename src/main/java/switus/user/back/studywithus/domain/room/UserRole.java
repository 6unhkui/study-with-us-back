package switus.user.back.studywithus.domain.room;

import java.util.Arrays;

public enum UserRole {
    MATE(0), MANAGER(99);

    private final int value;

    UserRole(int value) {
        this.value = value;
    }

    public static UserRole findByValue(int value){
        return Arrays.stream(UserRole.values())
                     .filter(v -> v.getValue() == value)
                     .findAny().orElse(null);
    }

    public int getValue() {
        return this.value;
    }
}
