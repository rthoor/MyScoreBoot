package ru.itis.myscore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.myscore.dto.LoginDto;
import ru.itis.myscore.exception.AccountAlreadyExistsException;
import ru.itis.myscore.model.Account;
import ru.itis.myscore.repository.AccountRepository;
import ru.itis.myscore.util.EmailUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService{
    private final AccountRepository accountRepository;
    private final EmailUtil emailUtil;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public boolean register(LoginDto loginDto) {
        boolean exists = accountRepository.findByEmail(loginDto.getEmail()).isPresent();
        if (!exists) {
            Account account = Account.builder()
                    .email(loginDto.getEmail())
                    .passwordHash(passwordEncoder.encode(loginDto.getPassword()))
                    .confirmCode(UUID.randomUUID().toString())
                    .state(Account.State.NOT_CONFIRMED)
                    .role(Account.Role.USER)
                    .build();

            accountRepository.save(account);
            Map<String, String> info = new HashMap<>();
            info.put("email", account.getEmail());
            info.put("confirm_code", account.getConfirmCode());

            emailUtil.sendMail("confirm", info);
            return true;
        } else {
            throw new AccountAlreadyExistsException("Account with email " + loginDto.getEmail() + " already exists");
        }
    }

    @Transactional
    @Override
    public boolean confirmAccount(UUID confirmCode) {
        Optional<Account> acc = accountRepository.findByConfirmCode(confirmCode.toString());
        if(acc.isPresent()){
            Account account = acc.get();
            account.setState(Account.State.CONFIRMED);
            accountRepository.save(account);
            return true;
        }
        return false;
    }
}
