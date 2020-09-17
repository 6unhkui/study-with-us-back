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
    private final CategoryService categoryService;

    private final AccountService accountService;
    private final FileService fileService;


    @Transactional
    public Long create(Long accountId, RoomDto.SaveRequest request, MultipartFile file) {
        Room room = request.toEntity();

        // 썸네일 파일이 존재하면 파일 업로드를 진행한다.
        if(null != file){
            FileInfo fileInfo;
            try {
                fileInfo = fileService.upload(file);
                room.setCover(fileInfo);
            } catch (IOException e) {
               throw new InternalServerException("파일 업로드 중에 에러가 발생했습니다.");
            }
        }

        // category id를 조회하고 존재하면 room entity에 category entity를 저장한다.
        Category category = categoryService.findById(request.getCategoryId());
        room.setCategory(category);

        // 생성한 스터디방을 저장한다.
        roomRepository.save(room);

        // 스터디 방을 만든 계정을 매니저로 가입시킨다
        Account account = accountService.findById(accountId);
        RoomMember roomMember = RoomMember.join(account, room, RoomMemberRole.MANAGER);

        return roomMemberRepository.save(roomMember).getId();
    }


    public Page<Room> findAll(RoomDto.SearchRequest searchRequest, Pageable pageable) {
        return roomRepository.findAll(searchRequest, pageable);
    }


    public Page<Room> findAllByAccountId(Long accountId, RoomDto.SearchRequest searchRequest, Pageable pageable) {
        return roomRepository.findAllByAccountId(accountId, searchRequest, pageable);
    }


    public Room findById(Long id) {
        return roomRepository.findById(id).orElseThrow(() -> new NoContentException("존재하지 않는 스터디방입니다."));
    }

    public Room findDetail(Long roomId) {
        return roomRepository.findDetailById(roomId);
    }

}
