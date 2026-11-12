package by.javaguru.users.data;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

public record CustomUserDetails(UserEntity userEntity) implements UserDetails {

    @Override
    public String getUsername() {
        return userEntity.getEmail();
    }

    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //  There are no roles yet, so we will return an empty list.
        return Collections.emptyList();
    }

}

/*
public class CustomUserDetails implements UserDetails {

    private final UserEntity userEntity;

    public CustomUserDetails(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    @Override
    public String getUsername() { return userEntity.getEmail(); }

    @Override
    public String getPassword() { return userEntity.getPassword(); }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //  There are no roles yet, so we will return an empty list.
        return Collections.emptyList();
    }

}
*/