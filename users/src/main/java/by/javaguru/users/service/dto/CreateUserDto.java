package by.javaguru.users.service.dto;

import lombok.Data;

@Data
//  A DTO class used when creating the entity.
//  It does not contain an `Id` field, as the ID will be generated upon entity creation.
public class CreateUserDto {

    private String firstName;
    private String lastName;
    private String email;
    private String password;

}
