package switus.user.back.studywithus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import switus.user.back.studywithus.common.util.FileUtils;
import switus.user.back.studywithus.domain.file.FileInfo;
import switus.user.back.studywithus.repository.FileRepository;

import java.io.IOException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final FileUtils fileUtils;

    @Transactional
    public FileInfo upload(MultipartFile file) throws IOException {
        return fileRepository.save(fileUtils.upload(file));
    }
}
