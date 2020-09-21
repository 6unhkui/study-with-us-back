package switus.user.back.studywithus.common.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import switus.user.back.studywithus.common.error.exception.BadRequestException;
import switus.user.back.studywithus.common.error.exception.InvalidFileAccessException;
import switus.user.back.studywithus.common.properties.FilePathProperties;
import switus.user.back.studywithus.domain.file.FileInfo;
import switus.user.back.studywithus.dto.FileDto;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileUtils {

    private final FilePathProperties filePathProperties;

    @PostConstruct
    public void init(){
        File coverImagePath = new File(filePathProperties.getCoverImage());
        File editorImagePath = new File(filePathProperties.getEditorImage());
        File attachment = new File(filePathProperties.getAttachment());

        if(!coverImagePath.exists()) {
            try {
                Path path = Paths.get(coverImagePath.getAbsolutePath()).normalize();
                Files.createDirectories(path);
            }catch (IOException e) {
               e.printStackTrace();
            }
        }

        if(!editorImagePath.exists()) {
            try {
                Path path = Paths.get(editorImagePath.getAbsolutePath()).normalize();
                Files.createDirectories(path);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(!attachment.exists()) {
            try {
                Path path = Paths.get(attachment.getAbsolutePath()).normalize();
                Files.createDirectories(path);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public FileInfo upload(FileDto.FileType fileType, MultipartFile file) throws IOException {
        String originName = StringUtils.cleanPath(Objects.requireNonNull(FilenameUtils.getName(file.getOriginalFilename())));
        String extension = FilenameUtils.getExtension(originName).toLowerCase();
        String saveName = generateUniqueFileName();

        Long size = file.getSize();

        Path dest = Paths.get(getPathPropForFileType(fileType))
                         .toAbsolutePath().normalize().resolve(saveName);
        Files.copy(file.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);

        return FileInfo.builder().originName(originName)
                                 .saveName(saveName)
                                 .savePath(dest.getParent().toString())
                                 .extension(extension)
                                 .fileSize(size)
                                 .build();
    }


    private String generateUniqueFileName() {
        return UUID.randomUUID().toString().replace("-", "");
    }


    public Resource loadAsResource(FileDto.FileType fileType, String saveName) {
        try {
            Path filePath = Paths.get(getPathPropForFileType(fileType), saveName);
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }else {
                throw new InvalidFileAccessException("Could not read file = " + saveName);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new InvalidFileAccessException(e.getMessage());
        }
    }


    public String getPathPropForFileType(FileDto.FileType fileType) {
        switch (fileType){
            case COVER:
                return filePathProperties.getCoverImage();
            case EDITOR:
                return filePathProperties.getEditorImage();
            default:
                return filePathProperties.getAttachment();
        }
    }

    public void isNotEmpty(MultipartFile file){
        if(file.isEmpty() || file.getSize() <= 0L) {
            throw new BadRequestException("MultipartFile size was 0 byte");
        }
    }

}
