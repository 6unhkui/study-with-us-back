package switus.user.back.studywithus.domain.common;

import lombok.Getter;
import switus.user.back.studywithus.domain.common.DateAudit;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class BaseEntity extends DateAudit {
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime delDate;

    @Column(columnDefinition = "TINYINT not null DEFAULT 0")
    private Boolean delFlag = Boolean.FALSE;

    public void delete() {
        this.delDate = LocalDateTime.now();
        this.delFlag = Boolean.TRUE;
    }
}
