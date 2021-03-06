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
import switus.user.back.studywithus.common.error.exception.BadRequestException;
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
import java.util.Optional;

@Api(tags = {"File"})
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
    public CommonResponse<FileDto.FileGroupResponse> uploadCoverImage(@RequestParam(value = "fileGroupId", required = false) Long fileGroupId,
                                                                      @RequestParam("file") MultipartFile file) {
        // 빈 파일 체크
        fileUtils.isNotEmpty(file);
        // 이미지 파일 체크
        imageUtils.verifyImageFile(file);

        if(null != fileGroupId) {
            // 커버 이미지를 변경할 경우
            return CommonResponse.success(fileService.changeFile(fileGroupId, FileDto.FileType.COVER, file));
        }else {
            // 파일을 서버에 업로드, 파일 테이블에 파일 정보 저장
            return CommonResponse.success(new FileDto.FileGroupResponse(fileService.upload(FileDto.FileType.COVER, file)));
        }
    }



    @ApiOperation("에디터 이미지 등록")
    @PostMapping(value = "/editor", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponse<FileDto.Response> uploadEditorImage(@RequestParam("file") MultipartFile file) {
        // 빈 파일 체크
        fileUtils.isNotEmpty(file);
        // 이미지 파일 체크
        imageUtils.verifyImageFile(file);

        // 파일을 서버에 저장
        FileInfo fileInfo = fileUtils.upload(FileDto.FileType.EDITOR, file);

        // 에디터 이미지는 html 안에 포함하고 있으므로 DB에 저장하지 않는다.
        return CommonResponse.success(new FileDto.Response(fileInfo));
    }


    @ApiOperation("게시글 첨부파일 등록")
    @PostMapping(value = "/attachment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponse uploadAttachment(@RequestParam(value = "fileGroupId", required = false) Long fileGroupId,
                                           @RequestParam("files") MultipartFile[] files) {
        // 요청 값으로 전달된 파일들이 빈 파일인지 체크한다.
        Arrays.stream(files).forEach(fileUtils::isNotEmpty);

        if(null != fileGroupId) {
            // 이미 등록된 파일 그룹에 파일을 추가로 업로드 할 경우
            return CommonResponse.success(new FileDto.FileGroupResponse(fileService.multiUpload(fileGroupId, FileDto.FileType.ATTACHMENT, files)));
        }else {
            return CommonResponse.success(new FileDto.FileGroupResponse(fileService.multiUpload(FileDto.FileType.ATTACHMENT, files)));
        }
    }


    @ApiOperation("파일 다운로드")
    @GetMapping("/{fileType}/{fileName}")
    public ResponseEntity<Resource> getAttachment(@PathVariable("fileType") String fileType,
                                                  @PathVariable("fileName") String fileName) throws IOException {
        FileDto.FileType type = Optional.of(FileDto.FileType.valueOf(fileType.toUpperCase()))
                                        .orElseThrow(() -> new BadRequestException("존재하지 않는 파일 형식입니다."));
        Resource resource = fileUtils.loadAsResource(type, fileName);
        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_OCTET_STREAM)
                             .cacheControl(CacheControl.noCache())
                             .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName + "")
                             .contentLength(resource.getFile().length())
                             .body(resource);
    }

}
