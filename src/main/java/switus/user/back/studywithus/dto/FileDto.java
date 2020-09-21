package switus.user.back.studywithus.dto;

import lombok.Data;
import switus.user.back.studywithus.domain.file.FileInfo;

public class FileDto {

    @Data
    public static class Response {
        private Long id;
        private String saveName;

        public Response(FileInfo fileInfo) {
            this.id = fileInfo.getId();
            this.saveName = fileInfo.getSaveName();
        }
    }
}
