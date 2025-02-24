package com.stasevich.taskmanagementsystembackend.user.controllers;

import com.stasevich.taskmanagementsystembackend.user.dto.JwtRequest;
import com.stasevich.taskmanagementsystembackend.user.dto.SaveUserDto;
import com.stasevich.taskmanagementsystembackend.user.dto.UserDto;
import com.stasevich.taskmanagementsystembackend.user.models.User;
import com.stasevich.taskmanagementsystembackend.user.services.AuthServiceImpl;
import com.stasevich.taskmanagementsystembackend.user.util.AuthException;
import com.stasevich.taskmanagementsystembackend.util.ErrorResponse;
import com.stasevich.taskmanagementsystembackend.user.util.ErrorsUtil;
import com.stasevich.taskmanagementsystembackend.user.util.UserException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthServiceImpl authService;
    private final ModelMapper modelMapper;

    @PostMapping("/sign-in")
    public ResponseEntity<?> createAuthToken(@RequestBody @Valid JwtRequest jwtRequest, BindingResult bindingResult) throws AuthException {

        if (bindingResult.hasErrors()){
            ErrorsUtil.returnAllErrors(bindingResult);
        }

        return ResponseEntity.ok(authService.createAuthToken(jwtRequest));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> createNewUser(@RequestBody @Valid SaveUserDto saveUserDto, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            ErrorsUtil.returnAllErrors(bindingResult);
        }

        User user = authService.registerNewUser(saveUserDto);

        return ResponseEntity.ok(converUserToUserDto(user));
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(AuthException ex) {
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(UserException ex) {
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.value()
                , ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    private UserDto converUserToUserDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}
