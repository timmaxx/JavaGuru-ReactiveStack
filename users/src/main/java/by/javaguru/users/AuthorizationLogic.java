package by.javaguru.users;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component("authz") // Имя бина в SpEL будет @authz
@RequiredArgsConstructor
public class AuthorizationLogic {

    /**
     * Проверяет, является ли текущий пользователь админом или тем, которым нужно управлять.
     * Возвращает Mono<Boolean> (в WebFlux это обязательно для неблокирующих запросов в БД).
     */
    public Mono<Boolean> canManageUser(UUID userId) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(auth -> {
                    // 1. Если это админ — сразу разрешаем
                    if (isAdmin(auth)) {
                        return Mono.just(true);
                    }
                    // 2. Иначе сверяем ид пользователя текущего и того, которым нужно управлять
                    return Mono.just(userId.toString().equals(auth.getName()));
                })
                .defaultIfEmpty(false); // Если контекст безопасности пуст (аноним) — запрещаем
    }

    private boolean isAdmin(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
