package switus.user.back.studywithus.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import switus.user.back.studywithus.common.error.exception.BadRequestException;
import switus.user.back.studywithus.common.error.exception.NoContentException;
import switus.user.back.studywithus.common.util.ImageUtils;
import switus.user.back.studywithus.domain.user.User;
import switus.user.back.studywithus.domain.user.UserRepository;
import switus.user.back.studywithus.payload.user.UserSaveRequest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
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
            throw new BadRequestException("이미 존재하는 회원입니다. email = " + user.getEmail());
        }
    }

    public User findByIdx(Long idx){
        return userRepository.findById(idx).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. idx=" + idx));
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    @Transactional
    public String updateProfileImg(Long idx, MultipartFile file) throws IOException {
        User user = findByIdx(idx);

        String filename = file.getOriginalFilename();
        String fileExtension = FilenameUtils.getExtension(filename);

        BufferedImage read = ImageIO.read(file.getInputStream());
        BufferedImage image = ImageUtils.makeThumbnail(read);

        String base64String = ImageUtils.getBase64String(image, fileExtension);
        user.setProfileImg(base64String);

        userRepository.save(user);
        return base64String;
    }
}
