package switus.user.back.studywithus.domain.board;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRoomComment is a Querydsl query type for RoomComment
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QRoomComment extends EntityPathBase<RoomComment> {

    private static final long serialVersionUID = -1168737800L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRoomComment roomComment = new QRoomComment("roomComment");

    public final switus.user.back.studywithus.domain.common.QBaseEntity _super = new switus.user.back.studywithus.domain.common.QBaseEntity(this);

    public final ListPath<RoomComment, QRoomComment> child = this.<RoomComment, QRoomComment>createList("child", RoomComment.class, QRoomComment.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> delDate = _super.delDate;

    //inherited
    public final BooleanPath delFlag = _super.delFlag;

    public final NumberPath<Integer> depth = createNumber("depth", Integer.class);

    public final NumberPath<Long> idx = createNumber("idx", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> insDate = _super.insDate;

    public final QRoomComment parent;

    public final NumberPath<Integer> seq = createNumber("seq", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updDate = _super.updDate;

    public final switus.user.back.studywithus.domain.user.QUser user;

    public QRoomComment(String variable) {
        this(RoomComment.class, forVariable(variable), INITS);
    }

    public QRoomComment(Path<? extends RoomComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRoomComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRoomComment(PathMetadata metadata, PathInits inits) {
        this(RoomComment.class, metadata, inits);
    }

    public QRoomComment(Class<? extends RoomComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.parent = inits.isInitialized("parent") ? new QRoomComment(forProperty("parent"), inits.get("parent")) : null;
        this.user = inits.isInitialized("user") ? new switus.user.back.studywithus.domain.user.QUser(forProperty("user")) : null;
    }

}

