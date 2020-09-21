package switus.user.back.studywithus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import switus.user.back.studywithus.domain.file.FileGroup;

public interface FileGroupRepository extends JpaRepository<FileGroup, Long> {
}
