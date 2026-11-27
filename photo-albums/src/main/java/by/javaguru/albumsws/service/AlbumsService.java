package by.javaguru.albumsws.service;

import java.util.UUID;

import by.javaguru.albumsws.service.dto.AlbumDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AlbumsService {

    Flux<AlbumDto> getAlbums(String userId);

	Mono<AlbumDto> createAlbum(Mono<AlbumDto> album);

    Mono<AlbumDto> getAlbum(UUID id, String userId);

    Mono<AlbumDto> updateAlbum(Mono<AlbumDto> album);

    Mono<Void> deleteAlbum(UUID id, String userId);

}
