package ru.itis.myscore.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.itis.myscore.model.JwtToken;
import ru.itis.myscore.repository.JwtTokenRepository;
import ru.itis.myscore.security.config.JwtSecurityConfiguration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;

@RequiredArgsConstructor
public class JwtTokenAuthorizationFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private final String secretKey;
    private final long jwtTokenLifeTime;
    private final JwtTokenRepository jwtTokenRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals(JwtSecurityConfiguration.LOGIN_URL)
                || request.getRequestURI().equals(JwtSecurityConfiguration.REGISTER_URL)
                || request.getRequestURI().equals(JwtSecurityConfiguration.CONFIRM_ACCOUNT_URL)) {
            filterChain.doFilter(request, response);
        } else {
            String tokenHeader = request.getHeader("Authorization");

            if (tokenHeader == null) {
                logger.warn("Token is missing");
                filterChain.doFilter(request, response);
            } else if (tokenHeader.startsWith("Bearer ")) {
                String token = tokenHeader.substring("Bearer ".length());

                try {
                    DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secretKey))
                            .build().verify(token);

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(token, null,
                                    Collections.singletonList(
                                            new SimpleGrantedAuthority(decodedJWT.getClaim("role").asString())));

                    Timestamp created = Timestamp.valueOf(decodedJWT.getClaim("jwtCreatedDate").asString());
                    if (!isTokenExpired(created) && !jwtTokenRepository.existsByToken(token)) {
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        filterChain.doFilter(request, response);
                    } else {
                        jwtTokenRepository.save(JwtToken.builder().token(token).build());
                        throw new JWTVerificationException("Token expired");
                    }
                } catch (JWTVerificationException e) {
                    sendForbidden(response);
                }
            } else {
                logger.warn("Wrong token format");
                sendForbidden(response);
            }
        }
    }

    boolean isTokenExpired(Timestamp created){
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp expires = new Timestamp(created.getTime() + jwtTokenLifeTime);
        return now.after(expires);
    }

    private void sendForbidden(HttpServletResponse response) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        objectMapper.writeValue(response.getWriter(), Collections.singletonMap("error", "User with token not found or token expired"));
    }
}
