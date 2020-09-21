package switus.user.back.studywithus.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import switus.user.back.studywithus.common.error.exception.BadRequestException;
import switus.user.back.studywithus.common.error.exception.AccountNotFoundException;
import switus.user.back.studywithus.common.util.ImageUtils;
import switus.user.back.studywithus.common.util.MultilingualMessageUtils;
import switus.user.back.studywithus.domain.account.Account;
import switus.user.back.studywithus.dto.AccountDto;
import switus.user.back.studywithus.repository.AccountRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {

    private final MultilingualMessageUtils message;
    private final ImageUtils imageUtils;

    private final AccountRepository accountRepository;
    private final FileService fileService;


    @Transactional
    public Account save(Account account){
        validateDuplicateAccount(account);
        accountRepository.save(account);
        return account;
    }

    @Transactional
    public Long save(AccountDto.SaveRequest request){
        Account account = request.toEntity();
        validateDuplicateAccount(account);
        accountRepository.save(account);
        return account.getId();
    }

    private void validateDuplicateAccount(Account account){
        accountRepository.findByEmail(account.getEmail()).ifPresent(m -> {
            throw new BadRequestException(message.makeMultilingualMessage("accountExists"));
        });
    }

    public Account findById(Long id){
        return accountRepository.findById(id).orElseThrow(AccountNotFoundException::new);
    }

    public Optional<Account> findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }


    @Transactional
    public String uploadProfileImg(Long id, MultipartFile file) throws IOException {
        Account account = findById(id);
        String base64 = imageUtils.getThumbnailAsBase64(file);
        account.changeProfileImg(base64);
        return base64;
    }


    @Transactional
    public void update(Long id, AccountDto.UpdateRequest request) {
        Account account = findById(id);
        account.changeName(request.getName());
    }


    @Transactional
    public void updatePassword(Long id, AccountDto.PasswordChangeRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Account account = findById(id);
        if (!passwordEncoder.matches(request.getOldPassword(), account.getPassword())) {
            throw new BadRequestException(message.makeMultilingualMessage("wrongPassword"));
        }else {
            account.changePassword(passwordEncoder.encode(request.getNewPassword()));
        }
    }

    @Transactional
    public void delete(Long id) {
        Account account = findById(id);
        account.delete();
    }
}
