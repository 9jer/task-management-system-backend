package com.stasevich.taskmanagementsystembackend.user.services;

import com.stasevich.taskmanagementsystembackend.user.dto.JwtRequest;
import com.stasevich.taskmanagementsystembackend.user.dto.JwtResponse;
import com.stasevich.taskmanagementsystembackend.user.dto.SaveUserDto;
import com.stasevich.taskmanagementsystembackend.user.models.User;

public interface AuthService {
    JwtResponse createAuthToken(JwtRequest jwtRequest);
    User registerNewUser(SaveUserDto registrationUser);
}
