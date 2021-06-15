package switus.user.back.studywithus.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import switus.user.back.studywithus.domain.member.Member;
import switus.user.back.studywithus.domain.member.MemberRole;

import java.time.LocalDateTime;
import java.util.List;

public class MemberDto {

    @Data
    public static class SearchRequest {
        public enum SortBy {
            NAME, JOIN_DATE, ROLE
        }

        private SortBy sortBy = SortBy.NAME;
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

        public Response(Long memberId, MemberRole role, String name, String email, String profileImg) {
            this.memberId = memberId;
            this.role = role;
            this.name = name;
            this.email = email;
            this.profileImg = profileImg;
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

        private Long postCount;

        private List<AttendanceDto.StatisticsResponse> attendanceStatistics;

        public DetailResponse(){}

        public DetailResponse(Member member, Long postCount, List<AttendanceDto.StatisticsResponse> attendanceStatistics){
            this.memberId = member.getId();
            this.role = member.getRole();
            this.joinDate = member.getInsDate();
            this.name = member.getAccount().getName();
            this.email = member.getAccount().getEmail();
            this.profileImg = member.getAccount().getProfileImg();
            this.postCount = postCount;
            this.attendanceStatistics = attendanceStatistics;
        }

        public DetailResponse(Member member, Long postCount){
            this.memberId = member.getId();
            this.role = member.getRole();
            this.joinDate = member.getInsDate();
            this.name = member.getAccount().getName();
            this.email = member.getAccount().getEmail();
            this.profileImg = member.getAccount().getProfileImg();
            this.postCount = postCount;
        }
    }
}
