package by.javaguru.users.service;

import reactor.core.publisher.Mono;

import java.util.Map;

public interface AuthenticationService {

    //  Note to self: perhaps it would be better to use AuthenticationRequest here instead of username and password?
    Mono<Map<String, String>> authenticate(String username, String password);
}
