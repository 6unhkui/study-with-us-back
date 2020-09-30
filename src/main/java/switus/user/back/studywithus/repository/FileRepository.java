package switus.user.back.studywithus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import switus.user.back.studywithus.domain.file.FileInfo;

import java.util.List;

public interface FileRepository extends JpaRepository<FileInfo, Long> {

    @Modifying
    @Query("UPDATE FileInfo f SET f.delFlag = true WHERE f.id IN (:fileIds)")
    void deleteFiles(@Param("fileIds") List<Long> fileIds);
}
