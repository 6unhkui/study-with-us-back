package switus.user.back.studywithus.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RoomRepositoryTest {
    
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private JPAQueryFactory queryFactory;
    
    @Test
    public void 유저가_가입한_스터디룸() throws Exception {
        //given
//        Long userIdx = 1L;
//
//        PagingRequest pagingRequest = new PagingRequest();
//        pagingRequest.setSize(10);
//        pagingRequest.setPage(0);
//        pagingRequest.setDirection(Sort.Direction.ASC);
//
//        RoomDto.SearchRequest request = new RoomDto.SearchRequest();
//        request.setUserIdx(userIdx);
//
//        //when
//        Page<Room> rooms = roomRepository.searchAll(request, pagingRequest.of());
//
//        //then
//        assertNotNull(rooms);


    }

}