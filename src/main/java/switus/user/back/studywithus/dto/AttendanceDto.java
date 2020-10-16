package switus.user.back.studywithus.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import switus.user.back.studywithus.domain.attendance.Attendance;
import switus.user.back.studywithus.domain.room.Room;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class AttendanceDto {

    @Data
    public static class SaveRequest {
        private String memo;

        public Attendance toEntity() {
           return Attendance.builder().memo(memo).build();
        }
    }


    @Data
    public static class Response {
        private List<MemberResponse> members;

        @JsonProperty("isRegistered")
        private boolean isRegistered;

        public Response(List<MemberResponse> members, boolean isRegistered) {
            this.members = members;
            this.isRegistered = isRegistered;
        }
    }


    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class MemberResponse {
        private Long memberId;
        private String name;
        private String profileImg;
        private AttendanceField attendance;

        @Data
        private class AttendanceField {
            private String memo;

            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", timezone = "Asia/Seoul")
            private LocalDateTime time;

            public AttendanceField(String memo, LocalDateTime time) {
                this.memo = memo;
                this.time = time;
            }
        }

        @Builder
        public MemberResponse(Long memberId, String name, String profileImg, String memo, LocalDateTime time) {
            this.memberId = memberId;
            this.name = name;
            this.profileImg = profileImg;
            if(Optional.ofNullable(time).isPresent()) {
                this.attendance = new AttendanceField(memo, time);
            }
        }
    }


    @Data
    public static class StatisticsResponse {
        private String name;
        private Long count;

        public StatisticsResponse(String name, Long count) {
            this.name = name;
            this.count = count;
        }
    }
}
