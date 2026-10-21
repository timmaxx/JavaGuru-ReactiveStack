package by.javaguru.users.service;

import by.javaguru.users.data.UserRepository;
import by.javaguru.users.service.dto.CreateUserDto;
import by.javaguru.users.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
// To inject class fields via the constructor.
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Mono<UserDto> createUser(Mono<CreateUserDto> createUserDtoMono) {

        return createUserDtoMono
                //  If `createUserDtoToUserEntity` returns null, then `mapNotNull` will return null wrapped in a `Mono`;
                //  consequently, the subsequent `flatMap` call will not trigger a `NullPointerException` — and,
                //  what’s more, the `save` operation won’t even be called!
                .mapNotNull(userMapper::createUserDtoToUserEntity)
                //  The previous `mapNotNull` will return a `Mono<UserEntity>`.
                //  If `map` were used here, the result would be a `Mono<Mono<UserEntity>>`.
                //  Using `flatMap` will return a `Mono<UserEntity>`.
                .flatMap(userRepository::save)
                .mapNotNull(userMapper::userEntityToUserDto);
    }

}
