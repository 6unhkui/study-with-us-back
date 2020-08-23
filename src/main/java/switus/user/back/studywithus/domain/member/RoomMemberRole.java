package switus.user.back.studywithus.domain.member;

import java.util.Arrays;

public enum RoomMemberRole {
    MATE(0), MANAGER(99);

    private final int value;

    RoomMemberRole(int value) {
        this.value = value;
    }

    public static RoomMemberRole findByValue(int value){
        return Arrays.stream(RoomMemberRole.values())
                .filter(v -> v.getValue() == value)
                .findAny().orElse(null);
    }

    public int getValue() {
        return this.value;
    }
}
