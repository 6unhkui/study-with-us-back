package switus.user.back.studywithus.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import switus.user.back.studywithus.domain.user.User;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    @Rollback(false)
    public void 회원가입() throws Exception {
        //given
        User user = new User();
        user.setName("심인경");
        userService.join(user);

        //when
        User findUser = userService.findOne(user.getIdx());

        //then
        assertEquals(findUser.getName(), user.getName());
    }
}