package switus.user.back.studywithus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import switus.user.back.studywithus.common.error.exception.NoContentException;
import switus.user.back.studywithus.common.error.exception.ResourceNotFoundException;
import switus.user.back.studywithus.common.util.MultilingualMessageUtils;
import switus.user.back.studywithus.domain.category.Category;
import switus.user.back.studywithus.domain.file.FileGroup;
import switus.user.back.studywithus.domain.member.Member;
import switus.user.back.studywithus.domain.member.MemberRole;
import switus.user.back.studywithus.domain.room.Room;
import switus.user.back.studywithus.domain.account.Account;
import switus.user.back.studywithus.dto.RoomDto;
import switus.user.back.studywithus.repository.member.MemberRepository;
import switus.user.back.studywithus.repository.post.PostRepository;
import switus.user.back.studywithus.repository.room.RoomRepository;

import java.util.Optional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final CategoryService categoryService;
    private final PostRepository postRepository;

    private final AccountService accountService;
    private final FileService fileService;
    private final MultilingualMessageUtils message;


    @Transactional
    public Long create(Long accountId, RoomDto.SaveRequest request) {
        Room room = request.toEntity();

        // 썸네일 이미지
        if(null != request.getFileGroupId()) {
            FileGroup file = fileService.findFileGroup(request.getFileGroupId());
            room.setCover(file);
        }

        // category id를 조회하고 존재하면 room entity에 category entity를 저장한다.
        Category category = categoryService.findById(request.getCategoryId());
        room.setCategory(category);

        // 생성한 스터디방을 저장한다.
        Room createdRoom = roomRepository.save(room);

        // 스터디 방을 만든 계정을 매니저로 가입시킨다
        Account account = accountService.findById(accountId);
        Member member = Member.join(account, room, MemberRole.MANAGER);
        memberRepository.save(member);

        return createdRoom.getId();
    }


    public Page<Room> findAll(RoomDto.SearchRequest searchRequest, Pageable pageable) {
        return roomRepository.findAll(searchRequest, pageable);
    }


    public Page<Room> findAllByAccount(Long accountId, RoomDto.SearchRequest searchRequest, Pageable pageable) {
        return roomRepository.findAllByAccount(accountId, searchRequest, pageable);
    }


    public Room findById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new NoContentException(message.makeMultilingualMessage("room.notExist")));
    }

    public Room findDetail(Long roomId) {
        return Optional.ofNullable(roomRepository.findDetail(roomId))
                .orElseThrow(() -> new NoContentException(message.makeMultilingualMessage("room.notExist")));
    }

    @Transactional
    public void delete(Room room) {
        room.delete();
    }

    @Transactional
    public FileGroup changeCover(Long roomId, Long coverId) {
        Room room = findById(roomId);
        FileGroup fileGroup = fileService.findFileGroup(coverId);
        room.setCover(fileGroup);

        return fileGroup;
    }

    @Transactional
    public Category changeCategory(Long roomId, Long categoryId) {
        Room room = findById(roomId);
        Category category = categoryService.findById(categoryId);
        room.setCategory(category);

        return category;
    }


    @Transactional
    public Room update(Long roomId, RoomDto.UpdateRequest request) {
        Room room = findById(roomId);

        room.editRoom(request.getName(), request.getDescription(), request.getMaxCount());
        return room;
    }

}
