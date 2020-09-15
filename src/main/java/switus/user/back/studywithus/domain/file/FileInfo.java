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

    @Column(nullable = false)
    private String originName;

    @Column(nullable = false)
    private String saveName;

    @Column(nullable = false)
    private Long fileSize;

    @Column(nullable = false)
    private String extension;


    @Builder
    public FileInfo(String originName, String saveName, Long fileSize, String extension) {
        this.originName = originName;
        this.saveName = saveName;
        this.fileSize = fileSize;
        this.extension = extension;
    }
}
