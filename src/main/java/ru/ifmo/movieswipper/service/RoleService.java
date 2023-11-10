package ru.ifmo.movieswipper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ifmo.movieswipper.constant.RoleConstants;
import ru.ifmo.movieswipper.model.Role;
import ru.ifmo.movieswipper.repository.RoleRepository;

import java.util.Optional;

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
