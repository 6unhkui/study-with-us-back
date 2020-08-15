package switus.user.back.studywithus.domain;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import switus.user.back.studywithus.domain.audit.DateAudit;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
public class BaseEntity extends DateAudit {
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime delDate;

    @Column(nullable = false)
    private Boolean delFlag = Boolean.FALSE;

    public void delete() {
        this.delFlag = Boolean.TRUE;
    }

    public void undelete() {
        this.delFlag = Boolean.FALSE;
    }
}
