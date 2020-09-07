package switus.user.back.studywithus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import switus.user.back.studywithus.domain.member.RoomMemberRole;
import switus.user.back.studywithus.domain.room.Room;


public class RoomDto {

    @Data
    @AllArgsConstructor
    public static class SaveRequest {
        private String name;
        private String description;
        private int maxCount;
        private boolean unlimited;

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
            NAME, INS_DATA, MEMBER_COUNT;
        }

//        private SearchType searchType;
        private OrderType orderType = OrderType.NAME;
        private String keyword;
    }

    @Data
    public static class RoomResponse {
        private Long idx;
        private String name;
        private String description;
        private RoomMemberRole role;
        private int maxCount;

        public RoomResponse(Room room){
            this.idx = room.getIdx();
            this.name = room.getName();
            this.description = room.getDescription();
            this.maxCount = room.getMaxCount();
            this.role = room.getRoomMembers().get(0).getRole();
        }
    }

    @Data
    public static class RoomDetailResponse {
        private Long idx;
        private String name;
        private String description;
        private int maxCount;

        public RoomDetailResponse(Room room){
            this.idx = room.getIdx();
            this.name = room.getName();
            this.description = room.getDescription();
            this.maxCount = room.getMaxCount();
        }
    }


}
