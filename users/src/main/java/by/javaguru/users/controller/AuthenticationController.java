package by.javaguru.users.controller;

import by.javaguru.users.service.dto.AuthenticationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
//  By analogy with another controller.
@RequestMapping("/login")
public class AuthenticationController {

    @PostMapping
    public Mono<ResponseEntity<Object>> login(@RequestBody Mono<AuthenticationRequest> authenticationRequestMono) {
        return Mono.just(ResponseEntity.ok().build());
    }

}
