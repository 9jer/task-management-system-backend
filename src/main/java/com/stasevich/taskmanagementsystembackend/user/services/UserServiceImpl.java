package com.stasevich.taskmanagementsystembackend.user.services;

import com.stasevich.taskmanagementsystembackend.user.models.Role;
import com.stasevich.taskmanagementsystembackend.user.models.User;
import com.stasevich.taskmanagementsystembackend.user.repositories.UserRepository;
import com.stasevich.taskmanagementsystembackend.user.util.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleServiceImpl roleService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public User createNewUser(User user){
        user.setRoles(List.of(roleService.getUserRole()));
        user.setCreatedAt(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public List<User> findAll(){
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(()->
                new UserException(String.format("User with id %s not found", id)));
    }

    @Override
    @Transactional
    public User assignAdminRole(Long userId){
        User user = getUserById(userId);
        Role adminRole = roleService.getAdminRole();

        if(user.getRoles().contains(adminRole)){
            throw new UserException("This user already has the admin role");
        }

        user.getRoles().add(adminRole);

        return userRepository.save(user);
    }
}
