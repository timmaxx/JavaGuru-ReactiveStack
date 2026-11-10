package by.javaguru.users.service;

import by.javaguru.users.data.UserEntity;
import by.javaguru.users.data.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final ReactiveAuthenticationManager reactiveAuthenticationManager;
    private final UserRepository userRepository;

    @Override
    public Mono<Map<String, String>> authenticate(
            String username, String password) {
        return reactiveAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password))
                //  If the operation succeeds, ignore its result and return a new Mono.
                //  You could also use `map` instead of `then`.
                //  This way, you can retrieve everything you need from the authentication object,
                //  avoiding a second trip to the database.
                .then(getUserDetails(username))
                .map(this::createAuthResponse);
    }

    private Mono<UserEntity> getUserDetails(String username) {
        return userRepository.findByEmail(username);
    }

    private Map<String, String> createAuthResponse(UserEntity user) {
        Map<String, String> result = new HashMap<>();
        result.put("userId", user.getId().toString());
        //  TODO replace with real JWT
        result.put("token", "JWT");
        return result;
    }

}
