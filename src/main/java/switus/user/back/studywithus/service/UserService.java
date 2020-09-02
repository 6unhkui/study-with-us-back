package switus.user.back.studywithus.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import switus.user.back.studywithus.common.error.exception.BadRequestException;
import switus.user.back.studywithus.common.error.exception.NoContentException;
import switus.user.back.studywithus.common.util.ImageUtils;
import switus.user.back.studywithus.common.util.MultilingualMessageUtils;
import switus.user.back.studywithus.domain.user.User;
import switus.user.back.studywithus.repository.UserRepository;
import switus.user.back.studywithus.payload.user.UserPasswordChangeRequest;
import switus.user.back.studywithus.payload.user.UserSaveRequest;
import switus.user.back.studywithus.payload.user.UserUpdateRequest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MultilingualMessageUtils message;

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
            throw new BadRequestException(message.makeMultilingualMessage("userExists", null));
        }
    }

    public User findByIdx(Long idx){
        return userRepository.findById(idx).orElseThrow(() -> new NoContentException(message.makeMultilingualMessage("userNotFound", null)));
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    @Transactional
    public String uploadProfileImg(Long idx, MultipartFile file) throws IOException {
        User user = findByIdx(idx);

        String filename = file.getOriginalFilename();
        String fileExtension = FilenameUtils.getExtension(filename);

        BufferedImage read = ImageIO.read(file.getInputStream());
        BufferedImage image = ImageUtils.makeThumbnail(read);

        String base64String = ImageUtils.getBase64String(image, fileExtension);
        user.changeProfileImg(base64String);

//        userRepository.save(user);
        return base64String;
    }

    @Transactional
    public void update(Long idx, UserUpdateRequest request) {
        User user = findByIdx(idx);
        user.changeName(user.getName());
    }


    @Transactional
    public void updatePassword(Long idx, UserPasswordChangeRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = findByIdx(idx);
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BadRequestException(message.makeMultilingualMessage("wrongPassword", null));
        }else {
            user.changePassword(passwordEncoder.encode(request.getNewPassword()));
        }
    }

}
