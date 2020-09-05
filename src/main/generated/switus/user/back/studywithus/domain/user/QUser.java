package switus.user.back.studywithus.domain.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 953359434L;

    public static final QUser user = new QUser("user");

    public final switus.user.back.studywithus.domain.common.QBaseEntity _super = new switus.user.back.studywithus.domain.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> delDate = _super.delDate;

    //inherited
    public final BooleanPath delFlag = _super.delFlag;

    public final StringPath email = createString("email");

    public final NumberPath<Long> idx = createNumber("idx", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> insDate = _super.insDate;

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final StringPath phoneNo = createString("phoneNo");

    public final StringPath profileImg = createString("profileImg");

    public final EnumPath<AuthProvider> provider = createEnum("provider", AuthProvider.class);

    public final EnumPath<UserRole> role = createEnum("role", UserRole.class);

    public final ListPath<switus.user.back.studywithus.domain.member.RoomMember, switus.user.back.studywithus.domain.member.QRoomMember> roomMembers = this.<switus.user.back.studywithus.domain.member.RoomMember, switus.user.back.studywithus.domain.member.QRoomMember>createList("roomMembers", switus.user.back.studywithus.domain.member.RoomMember.class, switus.user.back.studywithus.domain.member.QRoomMember.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updDate = _super.updDate;

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

