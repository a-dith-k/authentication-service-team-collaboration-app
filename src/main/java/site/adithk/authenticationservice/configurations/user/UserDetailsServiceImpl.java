package site.adithk.authenticationservice.configurations.user;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import site.adithk.authenticationservice.feignclients.UserManagementServiceClient;
import site.adithk.authenticationservice.feignclients.entities.UserEntity;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserManagementServiceClient userManagementClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

       UserEntity user= userManagementClient.getUserData(username);

        if (username.equals(user.getEmail()))
             return new UserDetailsImpl(user);

        throw new UsernameNotFoundException("User not found");

    }
}
