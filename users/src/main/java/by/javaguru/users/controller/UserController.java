package by.javaguru.users.controller;

import by.javaguru.users.service.dto.CreateUserDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping
    //  This status (201) is more appropriate for the successful creation of an entity.
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody @Valid Mono<CreateUserDto> createUserDto) {
        System.out.println("INFO. Call to the createUser method Either there was no validation (There in no @Valid), or validation was (There in @Valid) and was successful.");
        System.out.println("createUserDto = " + createUserDto);
    }

}
