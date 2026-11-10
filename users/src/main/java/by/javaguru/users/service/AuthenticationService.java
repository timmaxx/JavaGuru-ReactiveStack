package by.javaguru.users.service;

import by.javaguru.users.service.dto.AuthenticationRequest;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface AuthenticationService {

    Mono<Map<String, String>> authenticate(AuthenticationRequest authenticationRequest);
}
