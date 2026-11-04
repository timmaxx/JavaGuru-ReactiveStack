package by.javaguru.users.service;

import by.javaguru.users.data.UserRepository;
import by.javaguru.users.service.dto.CreateUserDto;
import by.javaguru.users.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Mono<UserDto> createUser(Mono<CreateUserDto> createUserDtoMono) {

        return createUserDtoMono
                .mapNotNull(userMapper::createUserDtoToUserEntity)
                .flatMap(userRepository::save)
                .mapNotNull(userMapper::userEntityToUserDto);
    }

    @Override
    public Mono<UserDto> getUserById(UUID id) {
        return userRepository.findById(id)
                .mapNotNull(userMapper::userEntityToUserDto);
    }

    @Override
    public Flux<UserDto> findAll(int page, int limit, String email) {
        Pageable pageable = PageRequest.of(page, limit);

        return
                //  Create Mono with email if email is not null, create Mono.empty() if email is null.
                Mono.justOrEmpty(email)
                        .map(String::trim)
                        //  If email blank create Mono.empty(). Or create Mono.
                        .filter(s -> !s.isBlank())
                        //  Select from DB with conditional and return Flux<UserEntity>.
                        .flatMapMany(e -> userRepository.findAllByEmailContainingIgnoreCase(e, pageable))
                        //  Map to UserDto.
                        .map(userMapper::userEntityToUserDto);
    }

}
