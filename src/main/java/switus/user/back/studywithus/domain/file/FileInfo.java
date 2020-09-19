//package switus.user.back.studywithus.domain.file;
//
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import org.hibernate.annotations.Where;
//import org.springframework.context.annotation.Primary;
//import switus.user.back.studywithus.domain.common.BaseEntity;
//
//import javax.persistence.*;
//
//import static javax.persistence.GenerationType.AUTO;
//import static lombok.AccessLevel.PROTECTED;
//
//@Entity
//@Table(name = "file")
//@Getter
//@IdClass(FilePrimaryKey.class)
//@Where(clause = "del_Flag = false")
//@NoArgsConstructor(access = PROTECTED)
//public class FileInfo extends BaseEntity {
//
//    @Id
//    @GeneratedValue(strategy = AUTO)
//    @Column(name = "idx")
//    private Long idx;
//
//    @Id
//    @Column(name = "group_idx")
//    private Long groupIdx;
//
//    private String originName;
//    private String saveName;
//    private Long fileSize;
//    private String extension;
//
//
//    @Builder
//    public FileInfo(String originName, String saveName, Long fileSize, String extension) {
//        this.originName = originName;
//        this.saveName = saveName;
//        this.fileSize = fileSize;
//        this.extension = extension;
//    }
//}


package switus.user.back.studywithus.domain.file;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import org.springframework.context.annotation.Primary;
import switus.user.back.studywithus.domain.common.BaseEntity;
import switus.user.back.studywithus.domain.member.converter.MemberRoleConverter;

import javax.persistence.*;

import static javax.persistence.GenerationType.AUTO;
import static lombok.AccessLevel.PROTECTED;

@Entity @Table(name = "file")
@Where(clause = "del_Flag = false")
@Getter @NoArgsConstructor(access = PROTECTED)
public class FileInfo extends BaseEntity {

    @Id @GeneratedValue(strategy = AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "group_id")
    private Long groupId;

    @Column(nullable = false, length = 255)
    private String originName;

    @Column(nullable = false, length = 255)
    private String saveName;

    @Column(nullable = false, length = 255)
    private String savePath;

    @Column(nullable = false)
    private Long fileSize;

    @Column(nullable = false, length = 50)
    private String extension;

    @Convert(converter = MemberRoleConverter.class)
    @Column(columnDefinition = "TINYINT not null comment '0 : Cover / 1 : Editor / 2 : Attachment'")
    private FileType fileType;


    @Builder
    public FileInfo(String originName, String saveName, String savePath, Long fileSize, String extension, FileType fileType) {
        this.originName = originName;
        this.saveName = saveName;
        this.savePath = savePath;
        this.fileSize = fileSize;
        this.extension = extension;
        this.fileType = fileType;
    }
}
