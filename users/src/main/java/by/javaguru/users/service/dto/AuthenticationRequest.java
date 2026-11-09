package by.javaguru.users.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
//  Exclude password for non-logging
@ToString(exclude = "password")
@AllArgsConstructor
public class AuthenticationRequest {

    private String email;
    private String password;
}
