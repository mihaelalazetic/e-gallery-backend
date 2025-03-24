
package com.egallery.service;

import com.egallery.model.entity.Role;
import java.util.List;
import java.util.UUID;

public interface RoleService {
    Role create(Role entity);
    Role getById(UUID id);
    List<Role> getAll();
    void delete(UUID id);
}
