package com.stasevich.taskmanagementsystembackend.user.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SaveUserDto {
    @NotEmpty(message = "Username should not be empty!")
    private String username;

    @NotEmpty(message = "Email should be not empty!")
    @Email(message = "Incorrect email!")
    private String email;

    @NotEmpty(message = "Password should be not empty!")
    private String password;

    @NotEmpty(message = "Please confirm the password!")
    private String confirmPassword;

    @NotEmpty(message = "Name should be not empty!")
    private String name;

    @NotEmpty(message = "Phone number should not be empty")
    private String phone;
}
