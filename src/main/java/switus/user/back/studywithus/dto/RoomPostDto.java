package switus.user.back.studywithus.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import switus.user.back.studywithus.common.util.ImageUtils;
import switus.user.back.studywithus.domain.member.RoomMember;
import switus.user.back.studywithus.domain.member.RoomMemberRole;
import switus.user.back.studywithus.domain.post.RoomPost;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class RoomPostDto {

    @Data
    public static class SaveRequest {
        @NotNull
        private String title;

        @NotNull
        private String content;

        public RoomPost toEntity() {
            return RoomPost.builder().title(title).content(content).build();
        }
    }


    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {
        private Long id;
        private String title;
        private String content;
        private String thumbnail;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdDate;

        private AccountDto.Response author;

        public Response(RoomPost roomPost){
            this.id = roomPost.getId();
            this.title = roomPost.getTitle();
            this.content = roomPost.getContent();
            this.createdDate = roomPost.getInsDate();
            this.author = new AccountDto.Response(roomPost.getAccount());
            this.thumbnail = ImageUtils.extractImageFromHtml(roomPost.getContent());
        }
    }

}
