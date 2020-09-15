package switus.user.back.studywithus.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import switus.user.back.studywithus.common.properties.FilePathProperties;
import switus.user.back.studywithus.common.util.FileUtils;

import javax.activation.MimetypesFileTypeMap;
import java.io.IOException;

@Api(tags = {"5. File"})
@RestController
@RequestMapping("/resource")
@RequiredArgsConstructor
public class FileApiController {

    private final FileUtils fileUtils;
    private final FilePathProperties filePathProperties;

    private final MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();

    @GetMapping(value = "/room/cover-image/{fileName}")
    public ResponseEntity<InputStreamResource> coverImage(@PathVariable("fileName") String fileName) throws IOException {
        Resource resource = fileUtils.loadAsResource(filePathProperties.getCoverImage(), fileName);
        return ResponseEntity.ok()
                             .contentType(MediaType.parseMediaType(mimeTypesMap.getContentType(fileName)))
                             .contentLength(resource.getFile().length())
                             .body(new InputStreamResource(resource.getInputStream()));
    }

}
