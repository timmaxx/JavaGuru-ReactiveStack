package by.javaguru.users.controller;

import by.javaguru.users.service.dto.CreateUserDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping
    public void createUser(@RequestBody CreateUserDto createUserDto) {
        System.out.println("INFO. Call to the createUser method Either there was no validation (There in no @Valid), or validation was (There in @Valid) and was successful.");
    }

}
