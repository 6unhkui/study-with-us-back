package switus.user.back.studywithus.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import switus.user.back.studywithus.common.util.ImageUtils;
import switus.user.back.studywithus.domain.post.Post;
import switus.user.back.studywithus.domain.post.PostComment;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PostDto {

    @Data
    public static class SaveRequest {
        @NotNull
        private String title;

        @NotNull
        private String content;

        public Post toEntity() {
            return Post.builder().title(title).content(content).build();
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

        private MemberDto.Response author;
        private int commentCount;

        public Response(Post post){
            this.id = post.getId();
            this.title = post.getTitle();
            this.content = post.getContent();
            this.createdDate = post.getInsDate();
            this.author = new MemberDto.Response(post.getMember());
            this.thumbnail = ImageUtils.extractImageFromHtml(post.getContent());
            this.commentCount = post.getComments().size();
        }
    }

}
