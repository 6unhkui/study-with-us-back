package switus.user.back.studywithus.domain.room;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRoom is a Querydsl query type for Room
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QRoom extends EntityPathBase<Room> {

    private static final long serialVersionUID = -453654966L;

    public static final QRoom room = new QRoom("room");

    public final switus.user.back.studywithus.domain.common.QBaseEntity _super = new switus.user.back.studywithus.domain.common.QBaseEntity(this);

    public final ListPath<switus.user.back.studywithus.domain.category.Category, switus.user.back.studywithus.domain.category.QCategory> categories = this.<switus.user.back.studywithus.domain.category.Category, switus.user.back.studywithus.domain.category.QCategory>createList("categories", switus.user.back.studywithus.domain.category.Category.class, switus.user.back.studywithus.domain.category.QCategory.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> delDate = _super.delDate;

    //inherited
    public final BooleanPath delFlag = _super.delFlag;

    public final StringPath description = createString("description");

    public final NumberPath<Long> idx = createNumber("idx", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> insDate = _super.insDate;

    public final StringPath name = createString("name");

    public final ListPath<switus.user.back.studywithus.domain.member.RoomMember, switus.user.back.studywithus.domain.member.QRoomMember> roomMembers = this.<switus.user.back.studywithus.domain.member.RoomMember, switus.user.back.studywithus.domain.member.QRoomMember>createList("roomMembers", switus.user.back.studywithus.domain.member.RoomMember.class, switus.user.back.studywithus.domain.member.QRoomMember.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updDate = _super.updDate;

    public QRoom(String variable) {
        super(Room.class, forVariable(variable));
    }

    public QRoom(Path<? extends Room> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRoom(PathMetadata metadata) {
        super(Room.class, metadata);
    }

}

