package switus.user.back.studywithus.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import switus.user.back.studywithus.domain.member.RoomMember;
import switus.user.back.studywithus.domain.member.RoomMemberRole;
import switus.user.back.studywithus.domain.room.Room;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.stream.Collectors;


public class RoomDto {

    @Getter @Setter
    @NoArgsConstructor
    public static class SaveRequest {
        @NotNull
        private String name;
        private String description;
        private boolean unlimited;
        private int maxCount;
        private Long categoryId;

        public Room toEntity(){
            if(unlimited) maxCount = 0;
            return Room.builder().name(name).description(description).maxCount(maxCount).build();
        }
    }

    @Getter @Setter
    public static class SearchRequest {
//        public enum SearchType {
//            ALL, NAME;
//        }

        public enum OrderType {
            NAME, INS_DATE, MEMBER_COUNT;
        }

//        private SearchType searchType;
        private OrderType orderType = OrderType.NAME;
        private String keyword;
        private long[] categoriesId;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {
        private Long id;
        private String name;
        private String description;
        private int maxCount;
        private int joinCount;

        private String category;
        private String coverImage;

        private AccountDto.Response manager;

        public Response(Room room, RoomMember manager, long joinCount){
            this.id = room.getId();
            this.name = room.getName();
            this.description = room.getDescription();
            this.maxCount = room.getMaxCount();
            this.category = room.getCategory().getName();
            this.joinCount = (int) joinCount;

            if(null != room.getCover())
                this.coverImage = room.getCover().getSaveName();

            if(null != manager){
                this.manager = new AccountDto.Response(manager.getAccount());
            }
        }
    }


    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DetailResponse {
        private Long id;
        private String name;
        private String description;
        private boolean unlimited = true;
        private int maxCount;
        private int joinCount;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
        private LocalDateTime createDate;

        private String category;
        private String coverImage;

        private AccountDto.Response manager;

        private CurrentUser currentUser = new CurrentUser();

        @Getter @Setter
        private class CurrentUser {
            private boolean isMember = false;
            private RoomMemberRole role;

            public CurrentUser() {}
        }

        public DetailResponse(Room room, RoomMember currentUserStatusByRoom){
            this.id = room.getId();
            this.name = room.getName();
            this.description = room.getDescription();
            this.createDate = room.getInsDate();
            this.category = room.getCategory().getName();

            if(room.getMaxCount() != 0) {
                this.unlimited = false;
                this.maxCount = room.getMaxCount();
            }

            if(null != room.getCover()) {
                this.coverImage = room.getCover().getSaveName();
            }

            if(null != currentUserStatusByRoom){
                currentUser.setMember(true);
                currentUser.setRole(currentUserStatusByRoom.getRole());
            }

            if(null != room.getRoomMembers()){
                manager = new AccountDto.Response(room.getRoomMembers().stream().filter(v -> v.getRole().equals(RoomMemberRole.MANAGER)).collect(Collectors.toList()).get(0).getAccount());
                joinCount = room.getRoomMembers().size();
            }
        }
    }

    @Data
    public static class joinRequest {
        private Long roomId;
    }

}
