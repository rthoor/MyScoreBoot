package ru.itis.myscore.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.myscore.dto.LoginDto;
import ru.itis.myscore.model.Account;
import ru.itis.myscore.repository.AccountRepository;
import ru.itis.myscore.security.details.AccountDetails;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

@Slf4j
public class JwtTokenAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;
    private final String secretKey;
    private final AccountRepository accountRepository;
    private final long jwtTokenLifeTime;

    public JwtTokenAuthenticationFilter(AccountRepository accountRepository, AuthenticationManager manager,
                                        ObjectMapper objectMapper, String secretKey, long jwtTokenLifeTime) {
        super(manager);
        this.objectMapper = objectMapper;
        this.secretKey = secretKey;
        this.accountRepository = accountRepository;
        this.jwtTokenLifeTime = jwtTokenLifeTime;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginDto loginDto = objectMapper.readValue(request.getReader(), LoginDto.class);
            log.info("Attempt authentication - email {}", loginDto.getEmail());

            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

            return super.getAuthenticationManager().authenticate(token);

        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        AccountDetails accountDetails = (AccountDetails) authResult.getPrincipal();
        Account account = accountDetails.getAccount();
        Timestamp created = new Timestamp(System.currentTimeMillis());
        account.setJwtCreated(created);
        accountRepository.save(account);

        String token = JWT.create()
                .withSubject(account.getId().toString())
                .withClaim("email", account.getEmail())
                .withClaim("role", account.getRole().toString())
                .withClaim("state", account.getState().toString())
                .withClaim("jwtCreatedDate", account.getJwtCreated().toString())
                .sign(Algorithm.HMAC256(secretKey));

        HashMap<String, Object> jsonResponse = new HashMap<>();
        jsonResponse.put("token", token);
        jsonResponse.put("created", created);
        jsonResponse.put("expires", new Timestamp(created.getTime() + jwtTokenLifeTime));
        objectMapper.writeValue(response.getWriter(), jsonResponse);
    }
}
