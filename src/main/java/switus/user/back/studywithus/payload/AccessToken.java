package switus.user.back.studywithus.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccessToken implements Serializable {
    private String name;
    private String email;
    private String accessToken;
//    private String refreshToken;
    private long expiration;
}

