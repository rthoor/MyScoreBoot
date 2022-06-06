package ru.itis.myscore.security.details;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.itis.myscore.repository.AccountRepository;

@RequiredArgsConstructor
@Service
public class AccountDetailsService implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new AccountDetails(accountRepository.findByEmail(email).orElseThrow(
                        () -> new UsernameNotFoundException("User not found")));
    }

}
