package switus.user.back.studywithus.domain.file;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import switus.user.back.studywithus.domain.common.BaseEntity;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.AUTO;
import static lombok.AccessLevel.PROTECTED;

@Entity @Table(name = "file")
@Where(clause = "del_Flag = false")
@Getter @NoArgsConstructor(access = PROTECTED)
public class FileInfo extends BaseEntity {

    @Id @GeneratedValue(strategy = AUTO)
    @Column(name = "id")
    private Long id;

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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "file_group_id")
    private FileGroup fileGroup;

    public void setFileGroup(FileGroup fileGroup) {
        this.fileGroup = fileGroup;
    }

    @Builder
    public FileInfo(String originName, String saveName, String savePath, Long fileSize, String extension) {
        this.originName = originName;
        this.saveName = saveName;
        this.savePath = savePath;
        this.fileSize = fileSize;
        this.extension = extension;
    }

}
