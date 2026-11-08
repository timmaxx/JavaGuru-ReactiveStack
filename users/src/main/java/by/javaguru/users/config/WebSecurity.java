package by.javaguru.users.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;


@Configuration
//  It turns on all reactive configs and registers filters and registers main web filter
//  org.springframework.security.web.server.WebFilterChainProxy
//  See Alt + n, SearchEveryWhere -> WebFilterChainProxy
//  WebFilterChainProxy is added to the Spring context and will be invoked for every request.
//  It keeps SecurityWebFilterChain. And we will configure it.
@EnableWebFluxSecurity
public class WebSecurity {

    @Bean
    SecurityWebFilterChain httpSecurityFilterChain(
            //  ServerHttpSecurity is reactive analog for HttpSecurity (MVC).
            ServerHttpSecurity http) {
        return http.authorizeExchange(
                        exchange ->
                                //  We allow POST requests to bypass authentication.
                                exchange.pathMatchers(HttpMethod.POST, "/users")
                                        .permitAll()
                                        //  And all other requests will undergo authentication.
                                        .anyExchange().authenticated())
                //  This application is a RESTful service not intended for use via a browser;
                //  therefore, CSRF protection is disabled.
                //  CSRF (Cross-Site Request Forgery).
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .build();
    }

}
