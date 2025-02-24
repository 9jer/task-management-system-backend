package com.stasevich.taskmanagementsystembackend.user.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class JwtRequest {

    @NotEmpty(message = "Email should be not empty!")
    private String email;

    @NotEmpty(message = "Password should be not empty!")
    private String password;
}
