package switus.user.back.studywithus.domain.member;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRoomMember is a Querydsl query type for RoomMember
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QRoomMember extends EntityPathBase<RoomMember> {

    private static final long serialVersionUID = 447168357L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRoomMember roomMember = new QRoomMember("roomMember");

    public final NumberPath<Long> idx = createNumber("idx", Long.class);

    public final EnumPath<RoomMemberRole> role = createEnum("role", RoomMemberRole.class);

    public final switus.user.back.studywithus.domain.room.QRoom room;

    public final switus.user.back.studywithus.domain.user.QUser user;

    public QRoomMember(String variable) {
        this(RoomMember.class, forVariable(variable), INITS);
    }

    public QRoomMember(Path<? extends RoomMember> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRoomMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRoomMember(PathMetadata metadata, PathInits inits) {
        this(RoomMember.class, metadata, inits);
    }

    public QRoomMember(Class<? extends RoomMember> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.room = inits.isInitialized("room") ? new switus.user.back.studywithus.domain.room.QRoom(forProperty("room")) : null;
        this.user = inits.isInitialized("user") ? new switus.user.back.studywithus.domain.user.QUser(forProperty("user")) : null;
    }

}

