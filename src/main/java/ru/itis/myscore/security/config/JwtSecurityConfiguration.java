package ru.itis.myscore.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import ru.itis.myscore.repository.AccountRepository;
import ru.itis.myscore.repository.JwtTokenRepository;
import ru.itis.myscore.security.filters.JwtTokenAuthenticationFilter;
import ru.itis.myscore.security.filters.JwtTokenAuthorizationFilter;

@EnableWebSecurity
public class JwtSecurityConfiguration extends WebSecurityConfigurerAdapter {

    public static final String LOGIN_URL = "/login";
    public static final String REGISTER_URL = "/register";
    public static final String CONFIRM_ACCOUNT_URL = "/confirm";
    public static final String LOGOUT_URL = "/logout";

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.lifeTime}")
    private long jwtTokenLifeTime;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService accountDetailsService;

    @Autowired
    private LogoutHandler customLogoutHandler;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JwtTokenRepository jwtTokenRepository;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JwtTokenAuthenticationFilter authenticationFilter =
                new JwtTokenAuthenticationFilter(accountRepository, authenticationManagerBean(),
                        objectMapper, secretKey, jwtTokenLifeTime);

        JwtTokenAuthorizationFilter authorizationFilter =
                new JwtTokenAuthorizationFilter(objectMapper, secretKey, jwtTokenLifeTime, jwtTokenRepository);

        authenticationFilter.setFilterProcessesUrl(LOGIN_URL);

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilter(authenticationFilter);
        http.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);
        http.logout().addLogoutHandler(customLogoutHandler).logoutUrl(LOGOUT_URL);
        http.authorizeRequests()
                .antMatchers(LOGIN_URL + "/**").permitAll()
                .antMatchers(REGISTER_URL + "/**").permitAll()
                .antMatchers(CONFIRM_ACCOUNT_URL + "/**").permitAll()
//                .antMatchers("/countries").permitAll()
                .antMatchers("/matches").permitAll()
//                .antMatchers("/players").permitAll()
                .antMatchers("/teams").permitAll()
                .antMatchers("/countries").hasAuthority("USER")
//                .antMatchers("/matches").hasAuthority("USER")
                .antMatchers("/players/**").hasAuthority("USER");
//                .antMatchers("/teams").hasAuthority("USER");


    }

}

