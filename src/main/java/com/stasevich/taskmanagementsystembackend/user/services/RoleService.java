package com.stasevich.taskmanagementsystembackend.user.services;

import com.stasevich.taskmanagementsystembackend.user.models.Role;

public interface RoleService {
    Role getAdminRole();
    Role getUserRole();
}
