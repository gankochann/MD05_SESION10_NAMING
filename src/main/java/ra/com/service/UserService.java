package ra.com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ra.com.dto.UserDto;
import ra.com.model.en.UserLogin;
import ra.com.dto.UserLoginDto;
import ra.com.model.User;
import ra.com.model.en.ROLE;
import ra.com.repository.IUser;
import ra.com.security.jwt.JwtService;

@Service
public class UserService {
    @Autowired
    private IUser iUser;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public User findUserByUsername(String username) {
        return iUser.findUserByUsername(username);
    }

    public User registerUser(UserDto userDto){
        User user = User
                .builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .email(userDto.getEmail())
                .phone(userDto.getPhone())
                .role(ROLE.USER)
                .status(true)
                .build();
        try {
            return iUser.save(user);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public UserLogin loginUser(UserLoginDto userLoginDto){
        User user = findUserByUsername(userLoginDto.getUsername());
        if(user != null && passwordEncoder.matches(userLoginDto.getUsername(), userLoginDto.getPassword())){
            return UserLogin.builder()
                    .username(user.getUsername())
                    .accessToken(jwtService.generateJwtToken(user.getUsername()))
                    .build();
        }else {
            return null;
        }
    }
}
