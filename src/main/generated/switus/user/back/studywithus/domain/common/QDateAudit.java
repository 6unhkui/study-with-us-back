package switus.user.back.studywithus.domain.common;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QDateAudit is a Querydsl query type for DateAudit
 */
@Generated("com.querydsl.codegen.SupertypeSerializer")
public class QDateAudit extends EntityPathBase<DateAudit> {

    private static final long serialVersionUID = -623776562L;

    public static final QDateAudit dateAudit = new QDateAudit("dateAudit");

    public final DateTimePath<java.time.LocalDateTime> insDate = createDateTime("insDate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> updDate = createDateTime("updDate", java.time.LocalDateTime.class);

    public QDateAudit(String variable) {
        super(DateAudit.class, forVariable(variable));
    }

    public QDateAudit(Path<? extends DateAudit> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDateAudit(PathMetadata metadata) {
        super(DateAudit.class, metadata);
    }

}

