package switus.user.back.studywithus.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;
import switus.user.back.studywithus.common.error.exception.BadRequestException;
import switus.user.back.studywithus.domain.account.Account;
import switus.user.back.studywithus.common.annotaion.CurrentUser;
import switus.user.back.studywithus.domain.category.Category;
import switus.user.back.studywithus.domain.member.Member;
import switus.user.back.studywithus.domain.member.MemberRole;
import switus.user.back.studywithus.domain.room.Room;
import switus.user.back.studywithus.dto.AccountDto;
import switus.user.back.studywithus.dto.CategoryDto;
import switus.user.back.studywithus.dto.RoomDto;
import switus.user.back.studywithus.dto.common.CommonResponse;
import switus.user.back.studywithus.dto.common.CurrentAccount;
import switus.user.back.studywithus.dto.common.PageRequest;
import switus.user.back.studywithus.service.MemberService;
import switus.user.back.studywithus.service.RoomService;

import javax.validation.Valid;
import java.util.Optional;

@Api(tags = {"Room"})
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class RoomApiController {

    private final RoomService roomService;
    private final MemberService memberService;


    @ApiOperation("스터디방 만들기")
    @PostMapping("room")
    public CommonResponse<Long> create(@ApiIgnore @CurrentUser CurrentAccount account,
                                       @Valid @RequestBody RoomDto.SaveRequest request) throws NotFoundException {
        return CommonResponse.success(roomService.create(account.getId(), request));
    }


    @ApiOperation("스터디방 리스트")
    @GetMapping("rooms")
    public CommonResponse<Page<RoomDto.Response>> rooms(RoomDto.SearchRequest searchRequest,
                                                        PageRequest pageRequest) throws NotFoundException {
        Page<Room> rooms = roomService.findAll(searchRequest, pageRequest.of());

        // 각 스터디방의 매니저 정보를 함께 찾아 넣어준다.
        return CommonResponse.success(rooms.map(room -> new RoomDto.Response(room, memberService.findManagerByRoomId(room.getId()))));
    }



    @ApiOperation("현재 사용자가 가입한 스터디방 리스트")
    @GetMapping("user/rooms")
    public CommonResponse<Page<RoomDto.Response>> roomsByCurrentAccount(@ApiIgnore @CurrentUser CurrentAccount account,
                                                                        RoomDto.SearchRequest searchRequest,
                                                                        PageRequest pageRequest) throws NotFoundException {
        Page<Room> rooms = roomService.findAllByAccount(account.getId(), searchRequest, pageRequest.of());
        return CommonResponse.success(rooms.map(room -> new RoomDto.Response(room, memberService.findManagerByRoomId(room.getId()))));
    }



    @ApiOperation("스터디방 상세 보기")
    @GetMapping("room/{roomId}")
    public CommonResponse<RoomDto.DetailResponse> room(@ApiIgnore @CurrentUser CurrentAccount account,
                                                       @PathVariable("roomId") Long roomId) throws NotFoundException {

        Room detail = roomService.findDetail(roomId);
        Member manager = memberService.findManagerByRoomId(roomId);
        Optional<Member> currentAccountMembership = memberService.findByAccountAndRoom(account.getId(), roomId);
        return CommonResponse.success(new RoomDto.DetailResponse(detail, manager, currentAccountMembership));
    }


    @ApiOperation("스터디방 삭제")
    @DeleteMapping("room/{roomId}")
    public CommonResponse delete(@ApiIgnore @CurrentUser CurrentAccount account,
                                 @PathVariable("roomId") Long roomId) {
        Member membership = memberService.findMembership(account.getId(), roomId);
        if(!membership.getRole().equals(MemberRole.MANAGER)) {
            throw new BadRequestException("매니저 아닙니다.");
        }

        roomService.delete(membership.getRoom());
        return CommonResponse.success();
    }


    @ApiOperation("스터디방 커버 이미지 변경")
    @PutMapping("room/{roomId}/cover")
    public CommonResponse changeCover(@ApiIgnore @CurrentUser CurrentAccount account,
                                      @PathVariable("roomId") Long roomId,
                                      @Param("coverId") Long coverId) {
        roomService.changeCover(roomId, coverId);
        return CommonResponse.success();
    }


    @ApiOperation("스터디방 카테고리 변경")
    @PutMapping("room/{roomId}/category")
    public CommonResponse changeCategory(@ApiIgnore @CurrentUser CurrentAccount account,
                                         @PathVariable("roomId") Long roomId,
                                         @Param("categoryId") Long categoryId) {
        Category category = roomService.changeCategory(roomId, categoryId);
        return CommonResponse.success(new CategoryDto.CategoryResponse(category));
    }


    @ApiOperation("스터디방 정보 변경")
    @PutMapping("room/{roomId}")
    public CommonResponse update(@ApiIgnore @CurrentUser CurrentAccount account,
                                 @PathVariable("roomId") Long roomId,
                                 @Valid @RequestBody RoomDto.UpdateRequest request) {
        roomService.update(roomId, request);
        return CommonResponse.success();
    }

}
