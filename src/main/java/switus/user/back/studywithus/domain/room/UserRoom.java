package switus.user.back.studywithus.domain.room;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import switus.user.back.studywithus.domain.room.converter.UserRoleConverter;
import switus.user.back.studywithus.domain.user.User;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "user_room_hub")
@NoArgsConstructor(access = PROTECTED)
@Getter
public class UserRoom {
    @Id @GeneratedValue(strategy = IDENTITY)
    private Long idx;

    @Convert(converter = UserRoleConverter.class)
    private UserRole role;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(referencedColumnName = "idx")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(referencedColumnName = "idx")
    private Room room;
}
