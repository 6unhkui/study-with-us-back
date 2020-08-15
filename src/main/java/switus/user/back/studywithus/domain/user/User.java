package switus.user.back.studywithus.domain.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import switus.user.back.studywithus.domain.BaseEntity;

import javax.persistence.*;
import java.sql.Blob;

@Entity
@NoArgsConstructor
@Getter
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private String userId;
    private String pw;
    private String name;
    private String phoneNo;
    @Lob
    private Blob profileImg;

    public void setName(String name) {
        this.name = name;
    }
}
