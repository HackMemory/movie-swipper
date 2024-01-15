package ru.ifmo.userservice.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.ifmo.userservice.constant.RoleConstants;
import ru.ifmo.userservice.model.Role;
import ru.ifmo.userservice.repository.RoleRepository;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

    public Optional<Role> getAdminRole() {
        return findByName(RoleConstants.ADMIN);
    }

    public Optional<Role> getModeratorRole() {
        return findByName(RoleConstants.MODERATOR);
    }

    public Optional<Role> getVipRole() {
        return findByName(RoleConstants.VIP);
    }

    public Optional<Role> getMemberRole() {
        return findByName(RoleConstants.MEMBER);
    }

    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

}
