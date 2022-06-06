package ru.itis.myscore.security.handlers;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import ru.itis.myscore.model.JwtToken;
import ru.itis.myscore.repository.JwtTokenRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.lifeTime}")
    private long jwtTokenLifeTime;

    private final ObjectMapper objectMapper;

    private final JwtTokenRepository jwtTokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String tokenHeader = request.getHeader("Authorization");

        if (tokenHeader != null) {
            if (tokenHeader.startsWith("Bearer ")) {
                String token = tokenHeader.substring("Bearer ".length());
                addTokenToBlackList(token);
            }
        }
    }

    private void addTokenToBlackList(String token) {
        jwtTokenRepository.save(JwtToken.builder().token(token).build());
    }
}
