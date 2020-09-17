package switus.user.back.studywithus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import switus.user.back.studywithus.common.error.exception.NoContentException;
import switus.user.back.studywithus.domain.account.Account;
import switus.user.back.studywithus.domain.post.RoomPost;
import switus.user.back.studywithus.domain.room.Room;
import switus.user.back.studywithus.dto.RoomPostDto;
import switus.user.back.studywithus.repository.post.RoomPostRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomPostService {

    private final RoomPostRepository roomPostRepository;

    private final AccountService accountService;
    private final RoomService roomService;


    @Transactional
    public Long create(Long accountId, Long roomId, RoomPostDto.SaveRequest request) {
        RoomPost post = request.toEntity();

        Account account = accountService.findById(accountId);
        Room room = roomService.findById(roomId);

        post.create(account, room);

        return roomPostRepository.save(post).getId();
    }

    public Page<RoomPost> findAll(Long roomId, Pageable pageable) {
        return roomPostRepository.findAll(roomId, pageable);
    }

    public RoomPost findById(Long roomId) {
        return roomPostRepository.findById(roomId).orElseThrow(() -> new NoContentException("존재하지 않는 게시물입니다."));
    }


}
