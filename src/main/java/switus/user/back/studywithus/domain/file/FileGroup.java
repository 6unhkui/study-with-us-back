package switus.user.back.studywithus.domain.file;

import lombok.Getter;
import lombok.NoArgsConstructor;
import switus.user.back.studywithus.domain.member.Member;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity @Getter
@NoArgsConstructor(access = PROTECTED)
public class FileGroup {

    @Id @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "fileGroup")
    private List<FileInfo> files = new ArrayList<>();

    public void addFile(FileInfo file) {
        file.setFileGroup(this);
        files.add(file);
    }

    public void addFiles(List<FileInfo> files) {
        files.forEach(v -> v.setFileGroup(this));
        this.files.addAll(files);
    }

    public static FileGroup setFiles(List<FileInfo> files) {
        FileGroup fileGroup = new FileGroup();
        files.forEach(file -> {
            fileGroup.files.add(file);
            file.setFileGroup(fileGroup);
        });
        return fileGroup;
    }
}
