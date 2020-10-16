package switus.user.back.studywithus.domain.template;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import switus.user.back.studywithus.domain.common.BaseEntity;
import switus.user.back.studywithus.domain.template.converter.SendTypeConverter;

import javax.persistence.*;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Where(clause = "del_Flag = false")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class SmtpSendTemplate extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = SendTypeConverter.class)
    @Column(columnDefinition = "CHAR not null Default 'R' comment 'R : Register / F : Forgot Password'")
    private SendType sendType = SendType.REGISTER;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private int validTime;
}
