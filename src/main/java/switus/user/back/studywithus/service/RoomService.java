package switus.user.back.studywithus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import switus.user.back.studywithus.common.error.exception.InternalServerException;
import switus.user.back.studywithus.common.error.exception.NoContentException;
import switus.user.back.studywithus.domain.category.Category;
import switus.user.back.studywithus.domain.file.FileInfo;
import switus.user.back.studywithus.domain.member.RoomMember;
import switus.user.back.studywithus.domain.member.RoomMemberRole;
import switus.user.back.studywithus.domain.room.Room;
import switus.user.back.studywithus.domain.account.Account;
import switus.user.back.studywithus.dto.RoomDto;
import switus.user.back.studywithus.repository.CategoryRepository;
import switus.user.back.studywithus.repository.member.RoomMemberRepository;
import switus.user.back.studywithus.repository.room.RoomRepository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final CategoryRepository categoryRepository;

    private final AccountService accountService;
    private final FileService fileService;

    @PostConstruct
    public void init() {
        List<Category> categories = categoryRepository.findAll();

        if (categories.size() == 0) {
            categories = new ArrayList<>();
            categories.add(Category.builder().name("외국어").build());
            categories.add(Category.builder().name("수능").build());
            categories.add(Category.builder().name("공무원").build());
            categories.add(Category.builder().name("취업").build());
            categories.add(Category.builder().name("자격증").build());
            categories.add(Category.builder().name("개발").build());
            categories.add(Category.builder().name("디자인").build());
            categories.add(Category.builder().name("기타").build());
            categories.forEach(categoryRepository::save);
        }
    }

    @Transactional
    public Long create(Long userIdx, RoomDto.SaveRequest request, MultipartFile file) {
        Room room = request.toEntity();
        if(file != null){
            FileInfo fileInfo;
            try {
                fileInfo = fileService.upload(file);
                room.setCover(fileInfo);
            } catch (IOException e) {
               throw new InternalServerException("파일 업로드 중에 에러가 발생했습니다.");
            }
        }

        categoryRepository.findById(request.getCategoryId()).ifPresent(room::addCategory);
        roomRepository.save(room);

        Account account = accountService.findById(userIdx);
        RoomMember roomMember = RoomMember.join(account, room, RoomMemberRole.MANAGER);

        return roomMemberRepository.save(roomMember).getId();
    }

    @Transactional
    public void join(Long userIdx, Long roomIdx) {
        Account account = accountService.findById(userIdx);
        Room room = findById(roomIdx);

        RoomMember roomMember = RoomMember.join(account, room, RoomMemberRole.MATE);
        roomMemberRepository.save(roomMember);
    }

    public Page<RoomDto.Response> findAllWithPagination(RoomDto.SearchRequest searchRequest, Pageable pageable) {
        return roomRepository.findAllWithPagination(searchRequest, pageable)
                             .map(room -> new RoomDto.Response(room,
                                     roomMemberRepository.findManagerByRoomId(room.getId()),
                                     roomMemberRepository.countByRoomId(room.getId())));
    }


    public Page<RoomDto.Response> findAllByUserIdWithPagination(Long userIdx, RoomDto.SearchRequest searchRequest, Pageable pageable) {
        return roomRepository.findAllByUserIdWithPagination(userIdx, searchRequest, pageable)
                             .map(room -> new RoomDto.Response(room,
                                            roomMemberRepository.findManagerByRoomId(room.getId()),
                                            roomMemberRepository.countByRoomId(room.getId())));
    }

    public Page<RoomDto.Response> findAllByCategoryIdWithPagination(Long categoryIdx, RoomDto.SearchRequest searchRequest, Pageable pageable) {
        return roomRepository.findAllByCategoryIdWithPagination(categoryIdx, searchRequest, pageable)
                .map(room -> new RoomDto.Response(room,
                        roomMemberRepository.findManagerByRoomId(room.getId()),
                        roomMemberRepository.countByRoomId(room.getId())));
    }

    public Room findById(Long id) {
        return roomRepository.findById(id).orElseThrow(() -> new NoContentException("존재하지 않는 스터디방입니다."));
    }

    public Room findDetail(Long roomId) {
        return roomRepository.findOneById(roomId);
    }


}
