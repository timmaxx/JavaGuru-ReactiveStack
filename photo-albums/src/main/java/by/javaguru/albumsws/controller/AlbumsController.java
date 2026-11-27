package by.javaguru.albumsws.controller;

import java.net.URI;
import java.security.Principal;
import java.util.UUID;

import by.javaguru.albumsws.service.dto.AlbumDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import by.javaguru.albumsws.service.AlbumsService;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/albums")
@RequiredArgsConstructor
public class AlbumsController {

    private final AlbumsService albumsService;

    @GetMapping
    public Flux<AlbumDto> getAlbums(Principal principal) {
        return albumsService.getAlbums(principal.getName())
                .filter(albumDto -> albumDto.getUserId().equals(principal.getName()));
    }

    @PostMapping
    public Mono<ResponseEntity<AlbumDto>> createAlbum(@Valid @RequestBody Mono<AlbumDto> album,
                                                      Principal principal) {
        return album.map(albumDto -> {
            albumDto.setUserId(principal.getName());
            return albumDto;
        }).flatMap(albumDto -> albumsService.createAlbum(Mono.just(albumDto)))
                .map(albumDto -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .location(URI.create("/albums/" + albumDto.getId()))
                        .body(albumDto));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<AlbumDto>> getAlbum(@PathVariable UUID id, Principal principal) {
        return albumsService.getAlbum(id, principal.getName())
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<AlbumDto>> updateAlbum(@PathVariable UUID id,
                                                      @Valid @RequestBody Mono<AlbumDto> album,
                                                      Principal principal) {
        return album.flatMap(albumDto -> {
            albumDto.setId(id);
            albumDto.setUserId(principal.getName());
            return albumsService.updateAlbum(Mono.just(albumDto));
        }).map(ResponseEntity::ok)
          .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteAlbum(@PathVariable UUID id, Principal principal) {
        return albumsService.deleteAlbum(id, principal.getName())
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
