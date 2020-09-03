package switus.user.back.studywithus.dto;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class AccessToken implements Serializable {
    private String accessToken;
    private long expiration;
}

