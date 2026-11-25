package by.javaguru.users.config;

import by.javaguru.users.service.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebSecurity {

    @Bean
    SecurityWebFilterChain httpSecurityFilterChain(ServerHttpSecurity http,
                                                   ReactiveAuthenticationManager authenticationManager,
                                                   JwtService jwtService) {

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtService);

        return http.authorizeExchange(
                        exchange -> exchange
                                .pathMatchers(HttpMethod.POST, "/users").permitAll()
                                .pathMatchers(HttpMethod.POST, "/login").permitAll()
                                .pathMatchers(HttpMethod.GET, "/users/stream").permitAll()
                                .anyExchange().authenticated())
                //  Register CORS
                //  (Cross-Origin Resource Sharing - resource sharing between different sources)
                //  - a browser security mechanism that allows a web page from one domain
                //  to make requests to a server on a different domain.
                .cors(corsSpec -> corsSpec.configurationSource(corsConfigurationSource()))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authenticationManager(authenticationManager)
                .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Setting up CORS.
    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        //  Which addresses are allowed to request resources and gain access.
        //  Either list specific addresses:
        //  configuration.setAllowedOrigins(List.of("http://localhost:8081"));
        //  Or allow all:
        configuration.setAllowedOrigins(List.of("*"));
        //  Which methods are allowed (GET, PUT, etc.).
        configuration.setAllowedMethods(List.of("*"));
        //  Which headers are allowed.
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}
