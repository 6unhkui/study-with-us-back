package switus.user.back.studywithus.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import switus.user.back.studywithus.domain.member.RoomMember;
import switus.user.back.studywithus.domain.member.RoomMemberRole;

public class RoomMemberDto {

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
        private String name;
        private String profileImg;
        private String email;
        private RoomMemberRole role;

        public Response(RoomMember roomMember){
            this.name = roomMember.getAccount().getName();
            this.email = roomMember.getAccount().getEmail();
            this.profileImg = roomMember.getAccount().getProfileImg();
            this.role = roomMember.getRole();
        }
    }
}
