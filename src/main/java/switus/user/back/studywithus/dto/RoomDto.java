package switus.user.back.studywithus.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import switus.user.back.studywithus.domain.member.RoomMember;
import switus.user.back.studywithus.domain.member.RoomMemberRole;
import switus.user.back.studywithus.domain.room.Room;

import javax.validation.constraints.NotNull;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;


public class RoomDto {

    @Data
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

    @Data
    public static class SearchRequest {
        public enum OrderType {
            NAME, CREATED_DATE, JOIN_COUNT
        }

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

        private RoomMemberDto.Response manager;

        public Response(Room room, RoomMember manager){
            this.id = room.getId();
            this.name = room.getName();
            this.description = room.getDescription();
            this.maxCount = room.getMaxCount();
            this.category = room.getCategory().getName();
            this.joinCount = room.getJoinCount();

            if(null != room.getCover())
                this.coverImage = room.getCover().getSaveName();

            if(null != manager){
                this.manager = new RoomMemberDto.Response(manager);
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

        private RoomMemberDto.Response manager;

        private currentAccount currentAccount = new currentAccount();

        @Getter @Setter
        private class currentAccount {
            private boolean isMember = false;
            private RoomMemberRole role;

            public currentAccount() {}
        }

        public DetailResponse(Room room, RoomMember manager, RoomMember currentAccountMembership){
            this.id = room.getId();
            this.name = room.getName();
            this.description = room.getDescription();
            this.createDate = room.getInsDate();
            this.joinCount = room.getJoinCount();
            this.category = room.getCategory().getName();

            if(room.getMaxCount() != 0) {
                this.unlimited = false;
                this.maxCount = room.getMaxCount();
            }

            if(null != room.getCover()) {
                this.coverImage = room.getCover().getSaveName();
            }

            if(null != currentAccountMembership){
                this.currentAccount.setMember(true);
                this.currentAccount.setRole(currentAccountMembership.getRole());
            }

            if(null != manager){
                this.manager = new RoomMemberDto.Response(manager);
            }
        }
    }

}
