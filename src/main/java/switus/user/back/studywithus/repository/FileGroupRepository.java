package switus.user.back.studywithus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import switus.user.back.studywithus.domain.file.FileGroup;

import java.util.List;

public interface FileGroupRepository extends JpaRepository<FileGroup, Long> {
    @Query("SELECT g FROM FileGroup g JOIN FETCH g.files f WHERE g.id =:fileGroupId AND f.delFlag = false")
    FileGroup findOneById(@Param("fileGroupId") Long fileGroupId);
}
