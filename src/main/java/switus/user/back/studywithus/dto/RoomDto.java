package switus.user.back.studywithus.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Value;
import switus.user.back.studywithus.domain.member.Member;
import switus.user.back.studywithus.domain.member.MemberRole;
import switus.user.back.studywithus.domain.room.Room;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;


public class RoomDto {

    @Data
    public static class SaveRequest {
        @NotNull
        private String name;
        private String description;
        private boolean unlimited;
        private int maxCount;
        private Long categoryId;
        private Long fileGroupId;

        public Room toEntity(){
            if(unlimited) maxCount = 0;
            return Room.builder().name(name).description(description).maxCount(maxCount).build();
        }
    }

    @Setter
    public static class UpdateRequest {
        @NotNull
        private String name;
        private String description;
        private boolean unlimited;
        private int maxCount;

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public int getMaxCount() {
            if(unlimited) maxCount = 0;
            return maxCount;
        }
    }

    @Data
    public static class SearchRequest {
        public enum SortBy {
            NAME, CREATED_DATE, JOIN_COUNT
        }

        private SortBy sortBy = SortBy.NAME;
        private String keyword;
        private long[] categoryIds;
    }


    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {
        private Long roomId;
        private String name;
        private String description;
        private int maxCount;
        private int joinCount;
        private String category;
        private String coverImage;

        private MemberDto.Response manager;

        public Response(Room room, Member manager){
            this.roomId = room.getId();
            this.name = room.getName();
            this.description = room.getDescription();
            this.maxCount = room.getMaxCount();
            this.category = room.getCategory().getName();
            this.joinCount = room.getJoinCount();

            if(null != room.getCover() && room.getCover().getFiles().size() > 0) {
                this.coverImage = room.getCover().getFiles().get(0).getSaveName();
            }

            if(null != manager){
                this.manager = new MemberDto.Response(manager);
            }
        }
    }


    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DetailResponse {
        private Long roomId;
        private String name;
        private String description;
        private boolean unlimited = true;
        private int maxCount;
        private int joinCount;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
        private LocalDateTime createDate;

        private Long categoryId;
        private String category;

        private Long coverGroupId;
        private String coverImage;

        private MemberDto.Response manager;

        @JsonProperty("isManager")
        private boolean isManager;

        @JsonProperty("isMember")
        private boolean isMember;


        public DetailResponse(Room room, Member manager, Optional<Member> currentAccountMembership){
            this.roomId = room.getId();
            this.name = room.getName();
            this.description = room.getDescription();
            this.createDate = room.getInsDate();
            this.joinCount = room.getJoinCount();
            this.categoryId = room.getCategory().getId();
            this.category = room.getCategory().getName();

            if(room.getMaxCount() != 0) {
                this.unlimited = false;
                this.maxCount = room.getMaxCount();
            }

            if(null != room.getCover() && room.getCover().getFiles().size() > 0) {
                this.coverGroupId = room.getCover().getId();
                this.coverImage = room.getCover().getFiles().get(0).getSaveName();
            }

            if(currentAccountMembership.isPresent()){
                this.isMember = true;
                this.isManager = currentAccountMembership.get().getRole().equals(MemberRole.MANAGER);
            }

            if(null != manager){
                this.manager = new MemberDto.Response(manager);
            }
        }
    }

}
