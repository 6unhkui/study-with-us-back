package switus.user.back.studywithus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import switus.user.back.studywithus.common.error.exception.NoContentException;
import switus.user.back.studywithus.common.util.FileUtils;
import switus.user.back.studywithus.domain.file.FileGroup;
import switus.user.back.studywithus.domain.file.FileInfo;
import switus.user.back.studywithus.dto.FileDto;
import switus.user.back.studywithus.repository.FileGroupRepository;
import switus.user.back.studywithus.repository.FileRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final FileGroupRepository fileGroupRepository;
    private final FileUtils fileUtils;

    @Transactional
    public FileGroup upload(FileDto.FileType fileType, MultipartFile file) throws IOException {
        FileInfo fileInfo = fileRepository.save(fileUtils.upload(fileType, file));
        return fileGroupRepository.save(FileGroup.setFiles(Collections.singletonList(fileInfo)));
    }

    @Transactional
    public FileGroup upload(FileDto.FileType fileType, MultipartFile[] files) throws IOException {
        List<FileInfo> filesInfo = new ArrayList<>();
        Arrays.stream(files).forEach(file -> {
            try {
                filesInfo.add(fileRepository.save(fileUtils.upload(fileType, file)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return fileGroupRepository.save(FileGroup.setFiles(filesInfo));
    }

    public FileGroup findFileGroup(Long fileGroupId) {
        return fileGroupRepository.findById(fileGroupId).orElseThrow(() -> new NoContentException("존재하지 않는 파일입니다."));
    }

}
