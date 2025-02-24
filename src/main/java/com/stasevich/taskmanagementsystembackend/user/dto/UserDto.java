package com.stasevich.taskmanagementsystembackend.user.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserDto {

    private Long id;

    @NotEmpty(message = "Username should not be empty!")
    private String username;

    @NotEmpty(message = "Email should be not empty!")
    @Email(message = "Incorrect email!")
    private String email;

    @NotEmpty(message = "Name should be not empty!")
    private String name;

    @NotEmpty(message = "Phone number should not be empty")
    private String phone;
}
