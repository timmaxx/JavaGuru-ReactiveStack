package by.javaguru.users.service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
//  DTO class used for an existing entity.
//  It includes an `id` field, since the identifier is already generated when the entity is created.
//  However, it does not include a 'password' field, as that field should not be transmitted.
public class UserDto {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;

}
