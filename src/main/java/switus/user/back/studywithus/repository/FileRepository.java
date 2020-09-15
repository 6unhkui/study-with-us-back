package switus.user.back.studywithus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import switus.user.back.studywithus.domain.file.FileInfo;

public interface FileRepository extends JpaRepository<FileInfo, Long> {
}
