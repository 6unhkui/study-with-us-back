package switus.user.back.studywithus.domain.attendance;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRoomAttendance is a Querydsl query type for RoomAttendance
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QRoomAttendance extends EntityPathBase<RoomAttendance> {

    private static final long serialVersionUID = 1100868613L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRoomAttendance roomAttendance = new QRoomAttendance("roomAttendance");

    public final switus.user.back.studywithus.domain.common.QBaseEntity _super = new switus.user.back.studywithus.domain.common.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> delDate = _super.delDate;

    //inherited
    public final BooleanPath delFlag = _super.delFlag;

    public final NumberPath<Long> idx = createNumber("idx", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> insDate = _super.insDate;

    public final switus.user.back.studywithus.domain.room.QRoom room;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updDate = _super.updDate;

    public final switus.user.back.studywithus.domain.user.QUser user;

    public QRoomAttendance(String variable) {
        this(RoomAttendance.class, forVariable(variable), INITS);
    }

    public QRoomAttendance(Path<? extends RoomAttendance> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRoomAttendance(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRoomAttendance(PathMetadata metadata, PathInits inits) {
        this(RoomAttendance.class, metadata, inits);
    }

    public QRoomAttendance(Class<? extends RoomAttendance> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.room = inits.isInitialized("room") ? new switus.user.back.studywithus.domain.room.QRoom(forProperty("room")) : null;
        this.user = inits.isInitialized("user") ? new switus.user.back.studywithus.domain.user.QUser(forProperty("user")) : null;
    }

}

