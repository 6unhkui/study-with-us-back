package switus.user.back.studywithus.dto;

import lombok.Data;
import switus.user.back.studywithus.domain.file.FileGroup;
import switus.user.back.studywithus.domain.file.FileInfo;

import java.util.List;
import java.util.stream.Collectors;

public class FileDto {

    public enum FileType{
        COVER, EDITOR, ATTACHMENT;
    }

    @Data
    public static class Response {
        private Long id;
        private String saveName;

        public Response(FileInfo file) {
            this.id = file.getId();
            this.saveName = file.getSaveName();
        }
    }

    @Data
    public static class FileGroupResponse {
        private Long fileGroupId;
        private List<FileDto.Response> files;

        public FileGroupResponse(FileGroup fileGroup) {
            this.fileGroupId = fileGroup.getId();
            this.files = fileGroup.getFiles().stream().map(Response::new).collect(Collectors.toList());
        }
    }
}
