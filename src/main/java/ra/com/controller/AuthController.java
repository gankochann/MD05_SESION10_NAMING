package ra.com.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ra.com.dto.UserDto;
import ra.com.model.en.UserLogin;
import ra.com.dto.UserLoginDto;
import ra.com.model.User;
import ra.com.security.jwt.JwtService;
import ra.com.security.principal.MyUserDetail;
import ra.com.service.UserService;

@RestController
@Slf4j
@RequestMapping("")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/api/v1/admin/auth/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto){
        User user = userService.registerUser(userDto);
        if(user!= null){
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>("register failed", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/api/v1/admin/auth/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDto userLoginDto) throws BadRequestException{
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLoginDto.getUsername(),
                            userLoginDto.getPassword()
                    )
            );
        }catch (AuthenticationException e){
            throw new BadRequestException("Invalid username or password");
        }
        MyUserDetail myUserDetail = (MyUserDetail) authentication.getPrincipal();
        String token = jwtService.generateJwtToken(myUserDetail.getUsername());
        UserLogin userLogin = UserLogin.builder()
                .username(userLoginDto.getUsername())
                .accessToken(token)
                .build();
        return new ResponseEntity<>(userLogin, HttpStatus.OK);
    }

}
