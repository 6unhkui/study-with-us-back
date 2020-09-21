package switus.user.back.studywithus.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import switus.user.back.studywithus.common.util.ImageUtils;
import switus.user.back.studywithus.domain.post.Post;
import switus.user.back.studywithus.domain.post.PostComment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PostCommentDto {

    @Data
    public static class SaveRequest {
        private String content;
        private int depth;
        private int seq;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {
        private Long id;
        private String content;
        private int depth;
        private int seq;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdDate;

        private List<Response> child;

        private MemberDto.Response author;

        public Response(PostComment comment){
            this.id = comment.getId();
            this.content = comment.getContent();
            this.depth = comment.getDepth();
            this.seq = comment.getSeq();
            this.createdDate = comment.getInsDate();
            this.author = new MemberDto.Response(comment.getMember());
            this.child = comment.getChild().stream().map(PostCommentDto.Response::new).collect(Collectors.toList());
        }
    }
}
