package by.javaguru.users.config;

import by.javaguru.users.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;

@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtService jwtService;

    //  To ensure our stack is non-blocking and asynchronous, all filters in the filter chain must be asynchronous.
    //  The filter will be invoked for every request that requires authentication.
    //  This is where we need to implement the logic for validating the JWT token.
    @Override
    public Mono<Void> filter(
            //  The request and response are here.
            ServerWebExchange exchange,
            //  Filter chain.
            WebFilterChain chain) {
        String token = extractToken(exchange);

        if (token == null) {
            //  When our filter’s turn comes up in the queue, we simply say "pass it on" — without any logic involved.
            //  In other words, we do nothing and pass it along to the next filter.
            return chain.filter(exchange);
        }

        return validateToken(token)
                .flatMap(isValid -> isValid ? authenticateAndContinue(token, exchange, chain)
                        : handleInvalidToken(exchange));
    }

    private Mono<? extends Void> authenticateAndContinue(String token, ServerWebExchange exchange, WebFilterChain chain) {
        return Mono.just(jwtService.extractTokenSubject(token))
                .flatMap(subject -> {
                    Authentication auth = new UsernamePasswordAuthenticationToken(subject, null,
                            Collections.emptyList());

                    return chain.filter(exchange)
                            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
                });
    }


    private Mono<? extends Void> handleInvalidToken(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    private String extractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7).trim();
        }

        return null;
    }

    private Mono<Boolean> validateToken(String token) {
        return jwtService.validateJwt(token);
    }
}
