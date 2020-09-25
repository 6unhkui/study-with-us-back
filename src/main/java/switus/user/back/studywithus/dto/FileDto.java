package switus.user.back.studywithus.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import switus.user.back.studywithus.domain.file.FileGroup;
import switus.user.back.studywithus.domain.file.FileInfo;

import java.util.List;
import java.util.stream.Collectors;

public class FileDto {

    public enum FileType{
        COVER, EDITOR, ATTACHMENT;

//        public static FileType getFileType(String fileType) {
//            switch (fileType.toUpperCase()){
//                case "COVER" :
//                    return FileType.COVER;
//                case "EDITOR" :
//                    return FileType.EDITOR;
//                default:
//                    return FileType.ATTACHMENT;
//            }
//        }
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {
        private Long fileId;
        private String saveName;
        private String originName;
        private Long fileSize;

        public Response(FileInfo file) {
            this.fileId = file.getId();
            this.saveName = file.getSaveName();
            this.originName = file.getOriginName();
            this.fileSize = file.getFileSize();
        }
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class FileGroupResponse {
        private Long fileGroupId;
        private List<FileDto.Response> files;

        public FileGroupResponse(FileGroup fileGroup) {
            this.fileGroupId = fileGroup.getId();
            this.files = fileGroup.getFiles().stream().map(Response::new).collect(Collectors.toList());
        }
    }
}
