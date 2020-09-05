package switus.user.back.studywithus.domain.board;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRoomBoard is a Querydsl query type for RoomBoard
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QRoomBoard extends EntityPathBase<RoomBoard> {

    private static final long serialVersionUID = -587625281L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRoomBoard roomBoard = new QRoomBoard("roomBoard");

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

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updDate = _super.updDate;

    public final switus.user.back.studywithus.domain.user.QUser user;

    public QRoomBoard(String variable) {
        this(RoomBoard.class, forVariable(variable), INITS);
    }

    public QRoomBoard(Path<? extends RoomBoard> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRoomBoard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRoomBoard(PathMetadata metadata, PathInits inits) {
        this(RoomBoard.class, metadata, inits);
    }

    public QRoomBoard(Class<? extends RoomBoard> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.room = inits.isInitialized("room") ? new switus.user.back.studywithus.domain.room.QRoom(forProperty("room")) : null;
        this.user = inits.isInitialized("user") ? new switus.user.back.studywithus.domain.user.QUser(forProperty("user")) : null;
    }

}

