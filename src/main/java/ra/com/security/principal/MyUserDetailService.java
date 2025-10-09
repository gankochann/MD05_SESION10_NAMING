package ra.com.security.principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ra.com.model.User;
import ra.com.service.UserService;

import java.util.Collection;
import java.util.List;

@Service
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    private UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findUserByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException(username);
        }else {
            Collection<GrantedAuthority> grantedAuthorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().toString()));
            return MyUserDetail.builder()
                    .user(user)
                    .authorities(grantedAuthorities)
                    .build();
        }

    }
}
