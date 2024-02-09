package com.example.kamil.user.service.auth;


import com.example.kamil.user.model.dto.LoginResponse;
import com.example.kamil.user.model.dto.UserDTO;
import com.example.kamil.user.model.entity.User;
import com.example.kamil.user.model.payload.LoginPayload;
import com.example.kamil.user.model.payload.RegisterPayload;
import com.example.kamil.user.model.security.LoggedInUserDetails;
import com.example.kamil.user.service.UserService;
import com.example.kamil.user.service.security.AccessTokenManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthBusinessServiceImpl implements AuthBusinessService{

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final AccessTokenManager accessTokenManager;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse login(LoginPayload payload) {
        authenticate(payload); // if success continue else throw error

        User user = userService.getUserByEmailForUserDetails(payload.getEmail());

        return prepareLoginResponse(user,payload.isRememberMe());

    }


//    @Override
//    public void register(RegisterPayload payload) {
//
//
//
////         Insert user
//        User user = UserEntityMapper.INSTANCE.fromRegisterPayloadToUser(
//                payload,
//                passwordEncoder.encode(payload.getPassword()),
//                roleService.getDefaultRole().getId()
//        );
//        userService.insertUser(user);
//
//    }


    @Override
    public void setAuthentication(String email) {
        LoggedInUserDetails userDetails = (LoggedInUserDetails) userDetailsService.loadUserByUsername(email);

        log.info("Logged in User details:");
        System.out.println(userDetails);
//     set user details to security context
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities())
        );
    }


    //  private util methods
    //refactorThis: use email instead of user ,then get user from userService via email
    private LoginResponse prepareLoginResponse(User user , boolean rememberMe){
        return     LoginResponse.builder()
                .accessToken(accessTokenManager.generate(user))
                .userInfo(
                        UserDTO.builder().firstName(user.getFirstName()).lastName(user.getLastName()).username(user.getUsername()).email(user.getEmail()).build()
                )
                .build();
        //todo: refactor user info
    }

    private void authenticate(LoginPayload request){

        try{
            authenticationManager.authenticate(
                    //Learn:
                    // This check is there any user with this credentials or not
                    // this is my payload password:request.getPassword()
                    // and in config we set UserDetails which is fetched from db ,
                    // there is also my (userDetails) password associated with this username(email)
                    new UsernamePasswordAuthenticationToken(request.getEmail() ,request.getPassword())
            );
        }
        catch (AuthenticationException ex){
            // todo: add custom exception
            // todo: fix this

          log.error(ex.getMessage());
        }
    }
}
