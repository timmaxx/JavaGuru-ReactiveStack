package by.javaguru.albumsws.service;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import by.javaguru.albumsws.data.AlbumRepository;
import by.javaguru.albumsws.service.dto.AlbumDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AlbumsServiceImpl implements AlbumsService {

    private final AlbumRepository albumRepository;
    private final AlbumMapper albumMapper;

    @Override
    public Flux<AlbumDto> getAlbums(String userId) {
        return albumRepository
                .findByUserId(userId)
                .map(albumMapper::albumEntityToAlbumDto);
    }

    @Override
    public Mono<AlbumDto> createAlbum(Mono<AlbumDto> albumMono) {
        return albumMono
                .map(albumMapper::albumDtoToAlbumEntity)
                .flatMap(albumRepository::save)
                .mapNotNull(albumMapper::albumEntityToAlbumDto);
    }

    @Override
    public Mono<AlbumDto> getAlbum(UUID id, String userId) {
        return albumRepository.findByIdAndUserId(id, userId)
                .map(albumMapper::albumEntityToAlbumDto);
    }

    @Override
    public Mono<AlbumDto> updateAlbum(Mono<AlbumDto> albumMono) {
        return albumMono.flatMap(album ->
                albumRepository.findByIdAndUserId(album.getId(), album.getUserId())
                        .flatMap(existingAlbum -> {
                            existingAlbum.setTitle(album.getTitle());
                            return albumRepository.save(existingAlbum);
                        })
                        .map(albumMapper::albumEntityToAlbumDto)
        );
    }

    @Override
    public Mono<Void> deleteAlbum(UUID id, String userId) {
        return albumRepository.deleteByIdAndUserId(id, userId);
    }

}
