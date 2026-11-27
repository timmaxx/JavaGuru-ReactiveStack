package by.javaguru.albumsws.data;

import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "album")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlbumEntity {

    @Id
    private UUID id;

    private String userId;

    private String title;
    
}
