package com.stasevich.taskmanagementsystembackend.user.services;

import com.stasevich.taskmanagementsystembackend.user.dto.JwtRequest;
import com.stasevich.taskmanagementsystembackend.user.dto.JwtResponse;
import com.stasevich.taskmanagementsystembackend.user.dto.SaveUserDto;
import com.stasevich.taskmanagementsystembackend.user.models.User;
import com.stasevich.taskmanagementsystembackend.user.security.CustomUserDetails;
import com.stasevich.taskmanagementsystembackend.user.security.CustomUserDetailsService;
import com.stasevich.taskmanagementsystembackend.user.util.AuthException;
import com.stasevich.taskmanagementsystembackend.user.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final UserServiceImpl userService;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public JwtResponse createAuthToken(JwtRequest jwtRequest) {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getEmail(), jwtRequest.getPassword()));
        } catch (BadCredentialsException e) {
            System.out.println("Bad credentials: " + jwtRequest.getEmail());
            throw new AuthException("Incorrect email or password!");
        }

        CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService
                .loadUserByUsername(jwtRequest.getEmail());

        String token = jwtTokenUtils.generateToken(userDetails);

        return new JwtResponse(token);
    }

    @Override
    @Transactional
    public User registerNewUser(SaveUserDto registrationUser) {

        if(!registrationUser.getPassword().equals(registrationUser.getConfirmPassword())){
            throw new AuthException("Passwords do not match!");
        }

        if(userService.findByUsername(registrationUser.getUsername()).isPresent()){
            throw new AuthException(String.format("Username %s already exists!", registrationUser.getUsername()));
        }

        if(userService.findByEmail(registrationUser.getEmail()).isPresent()){
            throw new AuthException(String.format("User with email %s already exists!", registrationUser.getEmail()));
        }

        return userService.createNewUser(convertSaveUserDtoToUser(registrationUser));
    }

    private User convertSaveUserDtoToUser(SaveUserDto saveUserDto) {
        return modelMapper.map(saveUserDto, User.class);
    }
}
