package by.javaguru.users.service;

import by.javaguru.users.service.dto.CreateUserDto;
import by.javaguru.users.service.dto.UserDto;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

//  We are integrating our service with SpringSecurityWebFlux.
//  And now the security component can call our UserService to retrieve a user by userName.
public interface UserService extends ReactiveUserDetailsService {

    Mono<UserDto> createUser(Mono<CreateUserDto> createUserDtoMono);

    Mono<UserDto> getUserById(UUID id);

    Flux<UserDto> findAll(int page, int limit);

}
