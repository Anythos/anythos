package net.anythos.security.security_config;

import lombok.AllArgsConstructor;
import net.anythos.user.entity.User;
import net.anythos.user.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(onConstructor = @__(@Lazy))
public class DetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public Details loadUserByUsername(String username) throws UsernameNotFoundException {
        return buildDetails(username);
    }

    private Details buildDetails(String username){
        return Details.builder().user(getUser(username)).build();
    }

    private User getUser(String username){
        try{
            return userService.getUserByUsername(username);
        } catch (UsernameNotFoundException exception){
            throw new UsernameNotFoundException(username);
        }
    }
}
