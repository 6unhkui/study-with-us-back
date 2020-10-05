package switus.user.back.studywithus.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import switus.user.back.studywithus.common.util.ImageUtils;
import switus.user.back.studywithus.domain.post.Post;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PostDto {

    @Data
    public static class SaveRequest {
        @NotEmpty @Size(max = 255)
        private String title;

        @NotEmpty
        private String content;

        private Long fileGroupId;

        public Post toEntity() {
            return Post.builder().title(title).content(content).build();
        }
    }


    @Data
    public static class UpdateRequest {
        @NotEmpty @Size(max = 255)
        private String title;

        @NotEmpty
        private String content;

        private Long fileGroupId;
        private Long[] delFiles;
    }


    @Data
    public static class SearchRequest {
        private String keyword;
    }


    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {
        private Long postId;
        private String title;
        private String content;
        private String thumbnail;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdDate;

        private MemberDto.Response writer;

        private int commentCount;
        private int fileCount;

        public Response(Post post){
            this.postId = post.getId();
            this.title = post.getTitle();
            this.content = post.getContent();
            this.createdDate = post.getInsDate();
            this.writer = new MemberDto.Response(post.getMember());
            this.thumbnail = ImageUtils.extractImageFromHtml(post.getContent());
            this.commentCount = post.getComments().size();

            if(null != post.getFileGroup()) {
                this.fileCount = post.getFileGroup().getFiles().size();
            }
        }
    }


    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DetailResponse {
        private Long roomId;
        private String roomName;
        private Long postId;
        private String title;
        private String content;
        private String thumbnail;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdDate;

        private MemberDto.Response writer;

        @JsonProperty("isWriter")
        private boolean isWriter;

        private Long fileGroupId;
        private List<FileDto.Response> files;

        public DetailResponse(Post post, Long currentAccountId){
            this.roomId = post.getMember().getRoom().getId();
            this.roomName = post.getMember().getRoom().getName();
            this.postId = post.getId();
            this.title = post.getTitle();
            this.content = post.getContent();
            this.createdDate = post.getInsDate();
            this.writer = new MemberDto.Response(post.getMember());
            this.isWriter = post.getMember().getAccount().getId().equals(currentAccountId);
            this.thumbnail = ImageUtils.extractImageFromHtml(post.getContent());

            if(null != post.getFileGroup()) {
                this.fileGroupId = post.getFileGroup().getId();
                this.files = post.getFileGroup().getFiles().stream().map(FileDto.Response::new).collect(Collectors.toList());
            }

        }
    }
}
