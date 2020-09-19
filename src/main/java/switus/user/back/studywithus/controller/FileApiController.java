package switus.user.back.studywithus.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;
import sun.nio.ch.IOUtil;
import switus.user.back.studywithus.common.annotaion.CurrentUser;
import switus.user.back.studywithus.common.error.exception.BadRequestException;
import switus.user.back.studywithus.common.error.exception.InternalServerException;
import switus.user.back.studywithus.common.properties.FilePathProperties;
import switus.user.back.studywithus.common.util.FileUtils;
import switus.user.back.studywithus.common.util.ImageUtils;
import switus.user.back.studywithus.common.util.MultilingualMessageUtils;
import switus.user.back.studywithus.domain.account.Account;
import switus.user.back.studywithus.domain.file.FileType;
import switus.user.back.studywithus.dto.common.CommonResponse;
import switus.user.back.studywithus.service.AccountService;

import javax.activation.MimetypesFileTypeMap;
import java.io.IOException;

@Api(tags = {"6. File"})
@RestController
@RequestMapping("/resource")
@RequiredArgsConstructor
public class FileApiController {

    private final AccountService accountService;
    private final MultilingualMessageUtils message;

//    private final ImageUtils imageUtils;
    private final FileUtils fileUtils;
    private final MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();


    @GetMapping("/cover/{fileName}")
    public byte[] getCoverImage(@PathVariable("fileName") String fileName) throws IOException {
        Resource resource = fileUtils.loadAsResource(FileType.COVER, fileName);
        return IOUtils.toByteArray(resource.getInputStream());
    }


    @ApiOperation(value = "프로필 이미지 등록", notes = "프로필 이미지를 리사이징 후 base64로 인코딩하여 저장하고, 그 문자열을 반환합니다.")
    @PostMapping("/profile")
    public CommonResponse<String> uploadProfileImg(@ApiIgnore @CurrentUser Account account,
                                                   @RequestParam("file") MultipartFile file) {
        if(file.isEmpty() || file.getSize() <= 0L) {
            throw new BadRequestException("MultipartFile size was 0 byte");
        }
        if (file.getContentType() != null && !file.getContentType().startsWith("image/")) {
            throw new BadRequestException("Invalid content-type");
        }

        try {
            return CommonResponse.success(accountService.uploadProfileImg(account.getId(), file));
        }catch (IOException e){
            throw new InternalServerException(message.makeMultilingualMessage("profileImageUploadError"));
        }
    }


//    @PostMapping("/room/{roomId}/upload/image")
//    public ResponseEntity<InputStreamResource> uploadEditorImage(@PathVariable("roomId") Long roomId,
//                                                                 @RequestParam(value = "file") MultipartFile file) throws IOException {
//        if(file.isEmpty() || file.getSize() <= 0L) {
//            throw new BadRequestException("MultipartFile size was 0 byte");
//        }
//        if (file.getContentType() != null && !file.getContentType().startsWith("image/")) {
//            throw new BadRequestException("Invalid content-type");
//        }
//
//
//    }
//
//
//    @GetMapping("/editor/{fileName}")
//    public ResponseEntity<InputStreamResource> getEditorImage(@PathVariable("fileName") String fileSaveName) throws IOException {
//        Resource resource = fileUtils.loadAsResource(FileType.EDITOR, fileSaveName);
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(mimeTypesMap.getContentType(fileSaveName)))
//                .contentLength(resource.getFile().length())
//                .body(new InputStreamResource(resource.getInputStream()));
//    }

}
