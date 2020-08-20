package switus.user.back.studywithus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import switus.user.back.studywithus.domain.user.User;
import switus.user.back.studywithus.domain.user.UserRepository;
import switus.user.back.studywithus.payload.user.UserSaveRequest;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Long save(UserSaveRequest request){
        User user = request.toEntity();
        validateDuplicateUser(user);
        userRepository.save(user);
        return user.getIdx();
    }

    private void validateDuplicateUser(User user){
        Optional<User> findUser = userRepository.findByEmail(user.getEmail());
        if(findUser.isPresent()){
            throw new IllegalArgumentException("이미 존재하는 회원입니다. email = " + user.getEmail());
        }
    }

    public User findByIdx(Long idx){
        return userRepository.findById(idx).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. idx=" + idx));
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
