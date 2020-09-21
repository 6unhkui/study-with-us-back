package switus.user.back.studywithus.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import switus.user.back.studywithus.common.error.exception.InternalServerException;
import switus.user.back.studywithus.common.util.FileUtils;
import switus.user.back.studywithus.common.util.ImageUtils;
import switus.user.back.studywithus.common.util.MultilingualMessageUtils;
import switus.user.back.studywithus.domain.file.FileGroup;
import switus.user.back.studywithus.domain.file.FileInfo;
import switus.user.back.studywithus.dto.FileDto;
import switus.user.back.studywithus.dto.common.CommonResponse;
import switus.user.back.studywithus.service.FileService;

import java.io.IOException;
import java.util.Arrays;

@Api(tags = {"6. File"})
@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileApiController {

    private final FileService fileService;
    private final MultilingualMessageUtils message;

    private final FileUtils fileUtils;
    private final ImageUtils imageUtils;


    @ApiOperation("커버 이미지 등록")
    @PostMapping(value = "/cover", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponse<FileDto.FileGroupResponse> uploadCoverImage(@RequestParam("file") MultipartFile file) {
        // 빈 파일 체크
        fileUtils.isNotEmpty(file);
        // 이미지 파일 체크
        imageUtils.verifyImageFile(file);
        try {
            // 파일을 서버에 업로드, 파일 테이블에 파일 정보 저장
            FileGroup fileGroup = fileService.upload(FileDto.FileType.COVER, file);

            return CommonResponse.success(new FileDto.FileGroupResponse(fileGroup));
        }catch (IOException e){
            throw new InternalServerException(message.makeMultilingualMessage("profileImageUploadError"));
        }
    }

    @ApiOperation("커버 이미지 다운로드")
    @GetMapping("/cover/{fileName}")
    public ResponseEntity<Resource> getCoverImage(@PathVariable("fileName") String fileName) throws IOException {
        Resource resource = fileUtils.loadAsResource(FileDto.FileType.COVER, fileName);
        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_OCTET_STREAM)
                             .cacheControl(CacheControl.noCache())
                             .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName + "")
                             .contentLength(resource.getFile().length())
                             .body(resource);
    }



    @ApiOperation("에디터 이미지 등록")
    @PostMapping(value = "/editor", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponse<FileDto.Response> uploadEditorImage(@RequestParam("file") MultipartFile file) {
        // 빈 파일 체크
        fileUtils.isNotEmpty(file);
        // 이미지 파일 체크
        imageUtils.verifyImageFile(file);
        try {
            // 파일을 서버에 저장
            FileInfo fileInfo = fileUtils.upload(FileDto.FileType.EDITOR, file);

            // 에디터 이미지는 html 안에 포함하고 있으므로 DB에 저장하지 않는다.
            return CommonResponse.success(new FileDto.Response(fileInfo));
        }catch (IOException e){
            throw new InternalServerException(message.makeMultilingualMessage("profileImageUploadError"));
        }
    }

    @ApiOperation("에디터 이미지 다운로드")
    @GetMapping("/editor/{fileName}")
    public ResponseEntity<Resource> getEditorImage(@PathVariable("fileName") String fileName) throws IOException {
        Resource resource = fileUtils.loadAsResource(FileDto.FileType.EDITOR, fileName);
        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_OCTET_STREAM)
                             .cacheControl(CacheControl.noCache())
                             .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName + "")
                             .contentLength(resource.getFile().length())
                             .body(resource);
    }


    @ApiOperation("게시글 첨부파일 등록")
    @PostMapping(value = "/attachment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponse uploadAttachment(@RequestParam("files") MultipartFile[] files) {
        // 요청 값으로 전달된 파일들이 빈 파일인지 체크한다.
        Arrays.stream(files).forEach(fileUtils::isNotEmpty);
        try {
            return CommonResponse.success(new FileDto.FileGroupResponse(fileService.upload(FileDto.FileType.ATTACHMENT, files)));
        } catch (IOException e) {
            throw new InternalServerException(message.makeMultilingualMessage("profileImageUploadError"));
        }
    }

    @ApiOperation("게시글 첨부파일 다운로드")
    @GetMapping(value = "/attachment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Resource> getAttachment(@PathVariable("fileName") String fileName) throws IOException {
        Resource resource = fileUtils.loadAsResource(FileDto.FileType.ATTACHMENT, fileName);
        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_OCTET_STREAM)
                             .cacheControl(CacheControl.noCache())
                             .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName + "")
                             .contentLength(resource.getFile().length())
                             .body(resource);
    }

}
