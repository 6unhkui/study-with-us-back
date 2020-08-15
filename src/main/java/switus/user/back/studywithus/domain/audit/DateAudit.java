package switus.user.back.studywithus.domain.audit;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import switus.user.back.studywithus.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class DateAudit {
    @CreatedDate
    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP NOT NULL")
    private LocalDateTime insDate;

    @LastModifiedDate
    @Column(nullable = false, columnDefinition = "TIMESTAMP NOT NULL")
    private LocalDateTime updDate;
}
