package com.stasevich.taskmanagementsystembackend.user.services;

import com.stasevich.taskmanagementsystembackend.user.models.Role;
import com.stasevich.taskmanagementsystembackend.user.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getAdminRole(){
        return roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(()->new RuntimeException("Role Admin not found!"));
    }

    @Override
    public Role getUserRole(){
        return roleRepository.findByName("ROLE_USER")
                .orElseThrow(()->new RuntimeException("Role User not found!"));
    }
}
