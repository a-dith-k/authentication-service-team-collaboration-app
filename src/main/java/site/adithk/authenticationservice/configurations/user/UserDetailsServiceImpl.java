package site.adithk.authenticationservice.configurations.user;



import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import site.adithk.authenticationservice.dtos.UserDataResponse;
import site.adithk.authenticationservice.exceptions.UserNotFoundException;
import site.adithk.authenticationservice.exceptions.UserNotVerifiedException;
import site.adithk.authenticationservice.feignclients.UserManagementServiceClient;
import site.adithk.authenticationservice.feignclients.entities.UserEntity;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserManagementServiceClient userManagementClient;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDataResponse userResponse = userManagementClient
                .getUserData(username).getBody();

        if(userResponse!=null&&!userResponse.getVerificationData().getIsVerified())
                throw new UserNotVerifiedException("user not verified");

        UserEntity user=modelMapper
               .map(userResponse,UserEntity.class);

        if (user.getEmail()!=null)
             return new UserDetailsImpl(user);

        throw new UserNotFoundException("User not found");

    }
}
