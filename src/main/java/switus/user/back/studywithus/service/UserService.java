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
import switus.user.back.studywithus.common.error.exception.UserNotFoundException;
import switus.user.back.studywithus.common.util.ImageUtils;
import switus.user.back.studywithus.common.util.MultilingualMessageUtils;
import switus.user.back.studywithus.domain.user.User;
import switus.user.back.studywithus.dto.UserDto;
import switus.user.back.studywithus.repository.UserRepository;

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


    /**
     * 유저 저장
     * @param UserDto.SaveRequest
     * @return idx
     */
    @Transactional
    public Long save(UserDto.SaveRequest request){
        User user = request.toEntity();
        validateDuplicateUser(user);
        userRepository.save(user);
        return user.getIdx();
    }

    private void validateDuplicateUser(User user){
        Optional<User> findUser = userRepository.findByEmail(user.getEmail());
        if(findUser.isPresent()){
            throw new BadRequestException(message.makeMultilingualMessage("userExists"));
        }
    }

    public User findByIdx(Long idx){
        return userRepository.findById(idx).orElseThrow(UserNotFoundException::new);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    /**
     *
     * @param idx
     * @param file
     * @return
     * @throws IOException
     */
    @Transactional
    public String uploadProfileImg(Long idx, MultipartFile file) throws IOException {
        User user = findByIdx(idx);

        String filename = file.getOriginalFilename();
        String fileExtension = FilenameUtils.getExtension(filename);

        BufferedImage read = ImageIO.read(file.getInputStream());
        BufferedImage image = ImageUtils.makeThumbnail(read);

        String base64String = ImageUtils.getBase64String(image, fileExtension);
        user.changeProfileImg(base64String);

        return base64String;
    }


    @Transactional
    public void update(Long idx, UserDto.UpdateRequest request) {
        User user = findByIdx(idx);
        user.changeName(request.getName());
    }


    @Transactional
    public void updatePassword(Long idx, UserDto.PasswordChangeRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = findByIdx(idx);
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BadRequestException(message.makeMultilingualMessage("wrongPassword"));
        }else {
            user.changePassword(passwordEncoder.encode(request.getNewPassword()));
        }
    }

    @Transactional
    public void deleteUser(Long idx) {
        User user = findByIdx(idx);
        user.delete();
    }

}
