package switus.user.back.studywithus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import switus.user.back.studywithus.common.error.exception.BadRequestException;
import switus.user.back.studywithus.common.error.exception.AccountNotFoundException;
import switus.user.back.studywithus.common.util.ImageUtils;
import switus.user.back.studywithus.common.util.MultilingualMessageUtils;
import switus.user.back.studywithus.domain.account.Account;
import switus.user.back.studywithus.dto.AccountDto;
import switus.user.back.studywithus.repository.AccountRepository;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {

    private final MultilingualMessageUtils message;
    private final ImageUtils imageUtils;

    private final AccountRepository accountRepository;


    @Transactional
    public Long create(Account account){
        validateDuplicateAccount(account);
        accountRepository.save(account);
        return account.getId();
    }

    private void validateDuplicateAccount(Account account){
        accountRepository.findByEmail(account.getEmail()).ifPresent(value -> {
            throw new BadRequestException(message.makeMultilingualMessage("account.isExist"));
        });
    }

    public Account findById(Long id){
        return accountRepository.findById(id).orElseThrow(AccountNotFoundException::new);
    }

    public Optional<Account> findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }


    @Transactional
    public String uploadProfileImg(Long id, String base64) {
        Account account = findById(id);
        account.changeProfileImg(base64);
        return base64;
    }


    @Transactional
    public void update(Long id, AccountDto.AccountUpdateRequest request) {
        Account account = findById(id);
        account.changeName(request.getName());
    }


    @Transactional
    public void updatePassword(Long id, AccountDto.PasswordChangeRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Account account = findById(id);
        if (!passwordEncoder.matches(request.getOldPassword(), account.getPassword())) {
            throw new BadRequestException(message.makeMultilingualMessage("account.wrongPassword"));
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
