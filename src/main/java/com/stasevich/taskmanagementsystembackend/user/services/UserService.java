package com.stasevich.taskmanagementsystembackend.user.services;

import com.stasevich.taskmanagementsystembackend.user.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    User createNewUser(User user);
    List<User> findAll();
    User getUserById(Long id);
    User assignAdminRole(Long userId);
}
