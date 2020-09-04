package switus.user.back.studywithus.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import switus.user.back.studywithus.domain.user.AuthProvider;
import switus.user.back.studywithus.domain.user.UserRole;

import switus.user.back.studywithus.domain.user.User;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest // JPA Repository 단위 테스트/ 다른 컴포넌트들은 로드하지 않고 @Entity만 읽어 Repository 내용을 테스트 할 수 있음.
             // 또한 @Transactional을 포함하고 있어 따로 롤백 하지 않아도 된다
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void 이메일로_유저찾기() throws Exception {
        String email = "tester@gmail.com";
        String name = "테스터";
        UserRole role = UserRole.USER;
        AuthProvider provider = AuthProvider.LOCAL;

        //given
        userRepository.save(User.builder().name(name).email(email).role(role).provider(provider).build());
        
        //when
        Optional<User> user = userRepository.findByEmail(email);

        //then
        assertNotNull("유저는 null이 아니어야 한다.", user);
        assertTrue("유저 객체는 존재해야 한다.", user.isPresent());
        assertEquals("검색한 유저와 저장한 유저의 이메일이 동일해야한다.", user.get().getEmail(), email);
    }
}