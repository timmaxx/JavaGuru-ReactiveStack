package by.javaguru.users.config;

import by.javaguru.users.service.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;


@Configuration
@EnableWebFluxSecurity
public class WebSecurity {

    @Bean
    SecurityWebFilterChain httpSecurityFilterChain(ServerHttpSecurity http,
                                                   ReactiveAuthenticationManager authenticationManager,
                                                   JwtService jwtService) {

        //   Registering the filter
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtService);

        return http.authorizeExchange(
                        exchange -> exchange
                                .pathMatchers(HttpMethod.POST, "/users").permitAll()
                                .pathMatchers(HttpMethod.POST, "/login").permitAll()
                                .anyExchange().authenticated())
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                //  In Lesson 2.12, it was already necessary to register a ReactiveAuthenticationManager.
                //  Without this, only the login and create operations would work,
                //  while the getUser and getUsers methods would fail!
                .authenticationManager(authenticationManager)
                //  Add a filter to the authorization phase (there can be various filter chains).
                .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                //  Specify what should be stored in the security context between requests, and how.
                //  By default, this would be WebSessionServerSecurityContextRepository, which means data is stored in a session.
                //  Using NoOpServerSecurityContextRepository.getInstance() indicates that nothing should be stored between requests.
                //  This means each request must be authenticated independently —
                //  which is exactly what we will achieve using a JWT token included in the header of every request
                //  that requires authentication (i.e., for the getUser and getUsers methods).
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
