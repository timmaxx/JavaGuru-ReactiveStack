package by.javaguru.users.data;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.util.UUID;

//  The class name (UserEntity) does not match the table name (users), so we use @Table(name = "users").
@Table(name = "users")
//  We do not use the @Data annotation, as it is not ideal for entities.
//  There are issues with methods eqals(), hashcode(), toString(). They can be cyclical.
//  Instead, we use @Getter and @Setter.
@Getter
@Setter
//  This annotation allows creating an instance as follows:
//  UserEntity ue1 = new UserEntity();
@NoArgsConstructor
//  This annotation allows creating an instance as follows:
//  UserEntity ue2 = new UserEntity(UUID.randomUUID(),"John", "Doe", "some@email.org", "qwerty");
@AllArgsConstructor
//  This annotation allows creating an instance as follows:
//  UserEntity ue3 = UserEntity.builder().firstName("John").lastName("Doe").email("").password("qwerty").build();
@Builder
//  We do not use the @Entity annotation because this is a reactive project and the class utilizes the R2DBC library.
//  R2DBC is not at all the same thing as JPA/Hibernate.
//  There is no ORM, no manager, no lazy loading, and no proxy is created.
//  r2dbc is reactive SQL mapping, and the relationships here are established manually (for example, via JOIN or multiple SQL queries).
public class UserEntity {
    //  We do not use the @Column annotation, as there is a match based on naming conventions.

    @Id
    private UUID id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;
}
