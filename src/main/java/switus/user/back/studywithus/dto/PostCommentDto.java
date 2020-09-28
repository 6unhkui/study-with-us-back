package switus.user.back.studywithus.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import switus.user.back.studywithus.domain.post.PostComment;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class PostCommentDto {

    @Data
    public static class SaveRequest {
        @NotNull
        private String content;
        private Long parentId;

        public PostComment toEntity() {
            return PostComment.builder().content(content).build();
        }
    }


    @Data
    public static class UpdateRequest {
        private String content;
    }


    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {
        private Long commentId;
        private Long parentId;
        private String content;
        private int seq;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdDate;
        private MemberDto.Response writer;

        public Response(PostComment comment){
            this.commentId = comment.getId();

            if(null != comment.getParent()) {
                this.parentId =comment.getParent().getId();
            }else this.parentId = 0L;

            this.content = comment.getContent();
            this.seq = comment.getSeq();
            this.createdDate = comment.getInsDate();
            this.writer = new MemberDto.Response(comment.getMember());
        }
    }
}
