package switus.user.back.studywithus.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import switus.user.back.studywithus.domain.member.Member;
import switus.user.back.studywithus.domain.member.MemberRole;

import java.time.LocalDateTime;

public class MemberDto {

    @Data
    public static class SearchRequest {
        public enum OrderType {
            NAME, JOIN_DATE, ROLE
        }

        private OrderType orderType = OrderType.NAME;
        private String keyword;
    }


    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {
        private Long memberId;
        private MemberRole role;
        private String name;
        private String profileImg;
        private String email;

        public Response(Member member){
            this.memberId = member.getId();
            this.role = member.getRole();
            this.name = member.getAccount().getName();
            this.email = member.getAccount().getEmail();
            this.profileImg = member.getAccount().getProfileImg();
        }
    }


    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DetailResponse {
        private Long memberId;
        private MemberRole role;
        private String name;
        private String profileImg;
        private String email;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
        private LocalDateTime joinDate;

        public DetailResponse(Member member){
            this.memberId = member.getId();
            this.role = member.getRole();
            this.joinDate = member.getInsDate();
            this.name = member.getAccount().getName();
            this.email = member.getAccount().getEmail();
            this.profileImg = member.getAccount().getProfileImg();
        }
    }
}
