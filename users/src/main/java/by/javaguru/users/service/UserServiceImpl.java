package by.javaguru.users.service;

import by.javaguru.users.data.UserRepository;
import by.javaguru.users.service.dto.CreateUserDto;
import by.javaguru.users.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    //  Вместо такого:
    //  usersSink = Sinks.many().multicast().onBackpressureBuffer();
    //  Определим это в SinkConfig.
    private final Sinks.Many<UserDto> usersSink;

    @Override
    public Mono<UserDto> createUser(Mono<CreateUserDto> createUserDtoMono) {
        return createUserDtoMono
                .map(userMapper::createUserDtoToUserEntity)
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(e -> e.setPassword(passwordEncoder.encode(e.getPassword())))
                .flatMap(userRepository::save)
                .map(userMapper::userEntityToUserDto)
                //  Each successfully created userDto will be passed to usersSink.tryEmitNext(),
                //  which will generate an onNext signal. This will result in the addition of elements to the Flux
                //  emitted by UserController :: Flux<UserDto> streamUsers().
                .doOnSuccess(usersSink::tryEmitNext);
    }

    @Override
    public Mono<UserDto> getUserById(UUID id) {
        return userRepository.findById(id)
                .mapNotNull(userMapper::userEntityToUserDto);
    }

    @Override
    public Flux<UserDto> findAll(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);

        return userRepository.findAllBy(pageable)
                .map(userMapper::userEntityToUserDto);
    }

    @Override
    public Flux<UserDto> streamUser() {
        return usersSink
                //  Create a cold Flux (i.e., each new subscriber creates a new subscription).
                .asFlux()
                //  Now create a hot Flux (i.e., everyone connects to the same subscription).
                .publish()
                //  Automatically connect to the source once N subscribers have connected.
                //  In our case, as soon as at least one subscriber appears, the ConnectableFlux connects to the source,
                //  and data begins to flow. In other words, the Sink starts emitting data, while any subsequent subscribers
                //  connect to the already active stream and will receive only new events.
                .autoConnect(1);
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByEmail(username)
                .map(userEntity -> User
                        .withUsername(userEntity.getEmail())
                        .password(userEntity.getPassword())
                        .authorities(new ArrayList<>())
                        .build());
    }

}
