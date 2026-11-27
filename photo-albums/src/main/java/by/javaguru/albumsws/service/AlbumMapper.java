package by.javaguru.albumsws.service;

import by.javaguru.albumsws.data.AlbumEntity;
import by.javaguru.albumsws.service.dto.AlbumDto;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring",
        builder = @Builder(disableBuilder = true))
public interface AlbumMapper {

    AlbumEntity albumDtoToAlbumEntity(AlbumDto albumDto);

    AlbumDto albumEntityToAlbumDto(AlbumEntity albumEntity);

}
