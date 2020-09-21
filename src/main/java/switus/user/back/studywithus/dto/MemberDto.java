package switus.user.back.studywithus.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import switus.user.back.studywithus.domain.member.Member;
import switus.user.back.studywithus.domain.member.MemberRole;

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
        private Long id;
        private String name;
        private String profileImg;
        private String email;
        private MemberRole role;

        public Response(Member member){
            this.id = member.getAccount().getId();
            this.name = member.getAccount().getName();
            this.email = member.getAccount().getEmail();
            this.profileImg = member.getAccount().getProfileImg();
            this.role = member.getRole();
        }
    }
}
