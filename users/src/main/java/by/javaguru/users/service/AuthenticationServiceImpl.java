package by.javaguru.users.service;

import by.javaguru.users.data.CustomUserDetails;
import by.javaguru.users.data.UserEntity;
import by.javaguru.users.service.dto.AuthenticationRequest;
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

    @Override
    public Mono<Map<String, String>> authenticate(AuthenticationRequest authenticationRequest) {
        return reactiveAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()))
                //  This way, you can retrieve everything you need from the authentication object,
                //  avoiding a second trip to the database.
                .map(auth -> {
                    Object principal = auth.getPrincipal();
                    if (principal == null) {
                        throw new NullPointerException("principal is null");
                    }
                    if (!(principal instanceof CustomUserDetails(UserEntity userEntity))) {
                        throw new IllegalStateException("Unexpected principal type: " + principal.getClass());
                    }

                    return createAuthResponse(userEntity);
                });
    }

    private Map<String, String> createAuthResponse(UserEntity user) {
        Map<String, String> result = new HashMap<>();
        result.put("userId", user.getId().toString());
        //  TODO replace with real JWT
        result.put("token", "JWT");
        return result;
    }

}
