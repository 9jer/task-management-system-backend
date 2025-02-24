package com.stasevich.taskmanagementsystembackend.user.controllers;

import com.stasevich.taskmanagementsystembackend.user.dto.UserDto;
import com.stasevich.taskmanagementsystembackend.user.dto.UsersResponse;
import com.stasevich.taskmanagementsystembackend.user.models.User;
import com.stasevich.taskmanagementsystembackend.user.services.UserServiceImpl;
import com.stasevich.taskmanagementsystembackend.util.ErrorResponse;
import com.stasevich.taskmanagementsystembackend.user.util.UserException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;
    private final ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public UsersResponse getAllUsers() {
        return new UsersResponse(userService.findAll().stream().map(this::convertUserToUserDto).toList());
    }

    @GetMapping("/info")
    public String currentUserInfo(Principal principal) {
        return principal.getName();
    }

    @PatchMapping("/{id}/assign-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> assignAdmin(@PathVariable Long id){
        User updatedUser = userService.assignAdminRole(id);

        return ResponseEntity.ok(convertUserToUserDto(updatedUser));
    }

    private UserDto convertUserToUserDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(UserException e) {
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.value()
                , e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
