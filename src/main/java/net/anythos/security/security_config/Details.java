package net.anythos.security.security_config;

import lombok.Builder;
import net.anythos.user.entity.Role;
import net.anythos.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

@Builder
public class Details implements UserDetails {
    @Serial
    private static final long serialVersionUID = 7502322939676542596L;
    private static final String ROLE_PREFIX = "ROLE_";
    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return mapFromUser(user.getRoles());
    }

    private Collection<? extends GrantedAuthority> mapFromUser(Set<Role> roles){
        return roles.stream().map(r-> new SimpleGrantedAuthority(ROLE_PREFIX + r.getName())).toList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isActive();
    }
}
