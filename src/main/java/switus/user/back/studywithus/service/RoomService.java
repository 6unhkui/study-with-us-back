package switus.user.back.studywithus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import switus.user.back.studywithus.common.error.exception.NoContentException;
import switus.user.back.studywithus.domain.category.Category;
import switus.user.back.studywithus.domain.file.FileGroup;
import switus.user.back.studywithus.domain.member.Member;
import switus.user.back.studywithus.domain.member.MemberRole;
import switus.user.back.studywithus.domain.room.Room;
import switus.user.back.studywithus.domain.account.Account;
import switus.user.back.studywithus.dto.RoomDto;
import switus.user.back.studywithus.repository.member.MemberRepository;
import switus.user.back.studywithus.repository.room.RoomRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final CategoryService categoryService;

    private final AccountService accountService;
    private final FileService fileService;


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
        roomRepository.save(room);

        // 스터디 방을 만든 계정을 매니저로 가입시킨다
        Account account = accountService.findById(accountId);
        Member member = Member.join(account, room, MemberRole.MANAGER);

        return memberRepository.save(member).getId();
    }


    public Page<Room> findAll(RoomDto.SearchRequest searchRequest, Pageable pageable) {
        return roomRepository.findAll(searchRequest, pageable);
    }


    public Page<Room> findAllByAccountId(Long accountId, RoomDto.SearchRequest searchRequest, Pageable pageable) {
        return roomRepository.findAllByAccount(accountId, searchRequest, pageable);
    }


    public Room findById(Long id) {
        return roomRepository.findById(id).orElseThrow(() -> new NoContentException("존재하지 않는 스터디방입니다."));
    }

    public Room findDetail(Long roomId) {
        return roomRepository.findDetail(roomId);
    }


    @Transactional
    public void delete(Room room) {
       room.delete();
    }

}
