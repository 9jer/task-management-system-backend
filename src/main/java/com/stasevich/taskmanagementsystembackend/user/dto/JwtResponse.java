package com.stasevich.taskmanagementsystembackend.user.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class JwtResponse {
    private String token;
}
