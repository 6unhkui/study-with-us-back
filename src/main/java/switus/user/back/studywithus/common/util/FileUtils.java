package switus.user.back.studywithus.common.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import switus.user.back.studywithus.common.error.exception.InvalidFileAccessException;
import switus.user.back.studywithus.common.properties.FilePathProperties;
import switus.user.back.studywithus.domain.file.FileInfo;

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
        if(!coverImagePath.exists()) {
            try {
                Path path = Paths.get(coverImagePath.getAbsolutePath()).normalize();
                Files.createDirectories(path);
            }catch (IOException e) {
               e.printStackTrace();
            }
        }
    }

    public FileInfo upload(MultipartFile file) throws IOException {
        String originName = StringUtils.cleanPath(Objects.requireNonNull(FilenameUtils.getName(file.getOriginalFilename())));
        String extension = FilenameUtils.getExtension(originName).toLowerCase();
        String saveName = generateFileName() + "." + extension;

        Long size = file.getSize();

        Path dest = Paths.get(filePathProperties.getCoverImage()).toAbsolutePath().normalize().resolve(saveName);
        Files.copy(file.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);

        return FileInfo.builder().originName(originName).saveName(saveName).extension(extension).fileSize(size).build();
    }


    private String generateFileName() {
        return UUID.randomUUID().toString().replace("-", "");
    }


    public Resource loadAsResource(String path, String saveName) {
        try {
            Path filePath = Paths.get(path, saveName);
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
}
