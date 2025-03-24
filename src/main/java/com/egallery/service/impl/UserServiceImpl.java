
package com.egallery.service.impl;

import com.egallery.model.entity.ApplicationUser;
import com.egallery.repository.ApplicationUserRepository;
import com.egallery.service.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements ApplicationUserService {

    @Autowired
    private ApplicationUserRepository userRepository;

    @Override
    public ApplicationUser create(ApplicationUser entity) {
        return userRepository.save(entity);
    }

    @Override
    public ApplicationUser getById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<ApplicationUser> getAll() {
        return userRepository.findAll();
    }

    @Override
    public void delete(UUID id) {
        userRepository.deleteById(id);
    }
}
