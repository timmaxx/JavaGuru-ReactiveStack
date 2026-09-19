package by.javaguru.users.controller;

import by.javaguru.users.service.dto.CreateUserDto;
import by.javaguru.users.service.dto.UserDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping
    public Mono<ResponseEntity<UserDto>> createUser(@RequestBody @Valid Mono<CreateUserDto> createUserDto) {

        return createUserDto.map(request -> new UserDto(UUID.randomUUID(),
                request.getFirstName(),
                request.getLastName(),
                request.getEmail()))
                .log()
                .map(userDto -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .location(URI.create("/users/" + userDto.getId()))
                        .body(userDto));
    }

}
