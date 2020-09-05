package switus.user.back.studywithus.domain.common;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QBaseEntity is a Querydsl query type for BaseEntity
 */
@Generated("com.querydsl.codegen.SupertypeSerializer")
public class QBaseEntity extends EntityPathBase<BaseEntity> {

    private static final long serialVersionUID = 896508467L;

    public static final QBaseEntity baseEntity = new QBaseEntity("baseEntity");

    public final QDateAudit _super = new QDateAudit(this);

    public final DateTimePath<java.time.LocalDateTime> delDate = createDateTime("delDate", java.time.LocalDateTime.class);

    public final BooleanPath delFlag = createBoolean("delFlag");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> insDate = _super.insDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updDate = _super.updDate;

    public QBaseEntity(String variable) {
        super(BaseEntity.class, forVariable(variable));
    }

    public QBaseEntity(Path<? extends BaseEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBaseEntity(PathMetadata metadata) {
        super(BaseEntity.class, metadata);
    }

}

