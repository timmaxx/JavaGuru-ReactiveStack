package by.javaguru.users.service;

import by.javaguru.users.data.UserRepository;
import by.javaguru.users.service.dto.CreateUserDto;
import by.javaguru.users.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Mono<UserDto> createUser(Mono<CreateUserDto> createUserDtoMono) {

        return createUserDtoMono
                .map(userMapper::createUserDtoToUserEntity)
                //  We use a thread pool for blocking and heavy operations.
                .publishOn(Schedulers.boundedElastic())
                //  And then, everything that follows will run in a separate thread pool.
                .doOnNext(e -> e.setPassword(passwordEncoder.encode(e.getPassword())))
                .flatMap(userRepository::save)
                .mapNotNull(userMapper::userEntityToUserDto);
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

}
