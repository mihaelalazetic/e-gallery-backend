
package com.egallery.service.impl;

import com.egallery.model.entity.Role;
import com.egallery.repository.RoleRepository;
import com.egallery.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role create(Role entity) {
        return roleRepository.save(entity);
    }

    @Override
    public Role getById(UUID id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    @Override
    public void delete(UUID id) {
        roleRepository.deleteById(id);
    }
}
